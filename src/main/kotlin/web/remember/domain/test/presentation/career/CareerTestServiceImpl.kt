package web.remember.domain.test.presentation.career

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import web.remember.domain.question.entity.QuestionCtype
import web.remember.domain.question.entity.QuestionGroup
import web.remember.domain.question.entity.TestType
import web.remember.domain.question.repository.QuestionRepository
import web.remember.domain.test.dto.RequestScoreUpdateDto
import web.remember.domain.test.entity.Test
import web.remember.domain.test.repository.TestRepository
import java.util.concurrent.TimeUnit

class CareerTestServiceImpl(
    private val testRepository: TestRepository,
    private val redisTemplate: RedisTemplate<String, String>,
    private val questionRepository: QuestionRepository,
) : CareerTestService {
    private var objectMapper = ObjectMapper()
    private lateinit var hashOperations: HashOperations<String, String, String>

    @PostConstruct
    fun init() {
        hashOperations = redisTemplate.opsForHash()
    }

    override fun startTest(
        questionMap: Map<String, List<String>>,
        memberId: String,
    ): String {
        if (questionMap.isEmpty()) {
            throw IllegalArgumentException("질문이 없습니다.")
        }
        if (!isUUID(memberId)) {
            throw IllegalArgumentException("잘못된 memberId입니다.")
        }
        val testEntity = testRepository.save(Test(memberId = memberId, testType = TestType.CAREER))
        questionMap
            .forEach { (group, questions) ->
                val redisKey = "$memberId:${testEntity.id}:$group"
                hashOperations.putAll(
                    redisKey,
                    questions
                        .mapIndexed { _, questionId -> questionId to "0" }
                        .toMap(),
                )
                // 1 시간 TTL 설정
                redisTemplate.expire(redisKey, 60 * 60 * 24, TimeUnit.SECONDS)
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

    override fun finishTest(
        memberId: String,
        testId: String,
    ) {
        val keys: Set<String> = redisTemplate.keys("$memberId:$testId:*")
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
                val data = questionRepository.findAnmAndScoreAndCtypeById(questionId)
                val anm: String = data[0]
                val weight: Int = data[1].toInt()
                val ctype: String = data[2]
                map[ctype] = map[ctype]!! + score.toInt()
                if (anm != "NULL") {
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
        testRepository.save(test)
    }

    private fun isUUID(uuid: String): Boolean =
        uuid.matches(Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\$"))
}
