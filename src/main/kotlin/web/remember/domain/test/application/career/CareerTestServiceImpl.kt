package web.remember.domain.test.application.career

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import web.remember.domain.error.CustomException
import web.remember.domain.question.application.dto.ResponseFinishTestDto
import web.remember.domain.question.entity.QuestionANM
import web.remember.domain.question.entity.QuestionCtype
import web.remember.domain.question.entity.QuestionGroup
import web.remember.domain.question.entity.TestType
import web.remember.domain.question.repository.QuestionRepository
import web.remember.domain.test.entity.Test
import web.remember.domain.test.presentation.career.dto.RequestScoreUpdateDto
import web.remember.domain.test.repository.RedisHashOperations
import web.remember.domain.test.repository.TestRepository

@Service
class CareerTestServiceImpl(
    private val testRepository: TestRepository,
    private val redisTemplate: RedisTemplate<String, String>,
    private val questionRepository: QuestionRepository,
    private val redisHashOperations: RedisHashOperations,
) : CareerTestService {
    private var objectMapper = ObjectMapper()
    private lateinit var hashOperations: HashOperations<String, String, String>

    @PostConstruct
    fun init() {
        try {
            hashOperations = redisTemplate.opsForHash()
            val conn = redisTemplate.connectionFactory?.connection
//            val serverInfo = conn?.serverCommands()?.info("server")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Transactional
    override fun startTest(
        questionMap: Map<String, Map<String, String>>,
        memberId: String,
    ): String {
        if (questionMap.isEmpty()) {
            throw CustomException("질문이 없습니다.")
        }
        val testEntity = testRepository.save(Test(memberId = memberId, testType = TestType.CAREER))
        questionMap
            .forEach { (group, questions) ->
                val groupKey = QuestionGroup.convertToEnglish(QuestionGroup.valueOf(group))
                val redisKey = "$memberId:${testEntity.id}:$groupKey"
                hashOperations.putAll(
                    redisKey,
                    questions
                        .map { (questionId, _) -> questionId to "0" }
                        .toMap(),
                )
            }
        return testEntity.id.toString()
    }

    override fun updateCareerAge(
        testId: String,
        memberId: String,
        age: String,
    ) {
        val key = "$memberId:$testId:age"
        hashOperations.put(key, "age", age)
    }

    override fun findDataById(testId: String): MutableMap<String, String> {
        val data = testRepository.findDataById(testId.toLong()) ?: throw CustomException("데이터를 찾을 수 없습니다.")
        return data
    }

    override fun updateScore(dto: RequestScoreUpdateDto) {
        dto.scoreMap.forEach { (questionId, score) ->
            val groupInKorean: String = questionRepository.findGroupById(questionId)
            val groupInEnglish = QuestionGroup.convertToEnglish(QuestionGroup.valueOf(groupInKorean))
            val key = "${dto.memberId}:${dto.testId}:$groupInEnglish"
            hashOperations.increment(key, questionId, score.toLong())
        }
    }

    @Transactional
    override fun finishTest(
        memberId: String,
        testId: String,
    ) {
        val pattern = "$memberId:$testId:*"
        val keys: Set<String> = redisHashOperations.scanKeys(pattern)
        val map = mutableMapOf<String, Int>()
        val age: String = hashOperations.get("$memberId:$testId:age", "age").toString()

        QuestionCtype.entries.forEach {
            map[it.name] = 0
        }

        keys.forEach {
            if (it.endsWith("age")) {
                return@forEach
            }
            val group = QuestionGroup.convertToKorean(it.split(":")[2]) ?: return@forEach
            if (!map.containsKey(group.name)) {
                map[group.name] = 0
            }
            val values = hashOperations.entries(it)
            values.forEach { (questionId, score) ->
                map[group.name] = map[group.name]!! + score.toInt()
                val data: ResponseFinishTestDto = questionRepository.findAnmsAndScoreAndCtypeById(questionId)
                val anms: List<String> = data.anms
                val weight: Int = data.score
                val ctype: String = data.ctype
                map[ctype] = map[ctype]!! + score.toInt()
                if (anms.isEmpty()) {
                    return@forEach
                }
                anms.forEach { anm ->
                    if (!map.containsKey(anm)) {
                        map[anm] = score.toInt() * weight
                    } else {
                        map[anm] = map[anm]!! + score.toInt() * weight
                    }
                }
            }
        }
        val converted: MutableMap<String, String> = map.mapValues { it.value.toString() }.toMutableMap()
        converted["age"] = age
        converted[QuestionANM.MOVE_ON.name] =
            (
                converted[QuestionANM.MOVE_ON.name]?.toInt() ?: 0 +
                    (
                        (
                            (
                                converted[QuestionANM.ABILITY.name]?.toInt()
                                    ?: 0
                            ) + (converted[QuestionANM.NETWORK_POWER.name]?.toInt() ?: 0)
                        ) / 3
                    )
            ).toString()
        val test = testRepository.findById(testId.toLong()).orElseThrow { CustomException("테스트를 찾을 수 없습니다.") }
        test.data = converted
        redisTemplate.delete(keys)
    }

    private fun isUUID(uuid: String): Boolean =
        uuid.matches(Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\$"))
}
