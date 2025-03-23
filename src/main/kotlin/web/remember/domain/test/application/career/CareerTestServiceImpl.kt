package web.remember.domain.test.application.career

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import web.remember.domain.error.CustomException
import web.remember.domain.question.entity.QuestionCtype
import web.remember.domain.question.entity.QuestionGroup
import web.remember.domain.question.entity.TestType
import web.remember.domain.question.repository.QuestionRepository
import web.remember.domain.question.repository.dto.ResponseFinishTestDto
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
            val serverInfo = conn?.serverCommands()?.info("server")
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
                val redisKey = "$memberId:${testEntity.id}:$group"
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

    override fun updateScore(dto: RequestScoreUpdateDto) {
        dto.scoreMap.forEach { (questionId, score) ->
            val group: String = questionRepository.findGroupById(questionId)
            val key = "${dto.memberId}:${dto.testId}:$group"
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
        val testType = TestType.CAREER
        val map = mutableMapOf<String, Int>()
        val age: String = hashOperations.get("$memberId:$testId:age", "age").toString()

        QuestionCtype.entries.forEach {
            map[it.name] = 0
        }

        keys.forEach {
            if (it.endsWith("age")) {
                return@forEach
            }
            val group = QuestionGroup.valueOf(it.split(":")[2])
            if (!map.containsKey(group.name)) {
                map[group.name] = 0
            }
            val values = hashOperations.entries(it)
            values.forEach { (questionId, score) ->
                map[group.name] = map[group.name]!! + score.toInt()
                val data: ResponseFinishTestDto = questionRepository.findAnmsAndScoreAndCtypeById(questionId)
                val anms: List<String>? = data.anms
                val weight: Int = data.score
                val ctype: String = data.ctype
                map[ctype] = map[ctype]!! + score.toInt()
                if (anms == null) {
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
        val test =
            Test(
                memberId = memberId,
                testType = testType,
                data = converted,
            )
        redisTemplate.delete(keys)
        testRepository.save(test)
    }

    private fun isUUID(uuid: String): Boolean =
        uuid.matches(Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\$"))
}
