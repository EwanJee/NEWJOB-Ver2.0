package web.remember.domain.question.application.career

import org.springframework.stereotype.Service
import web.remember.domain.error.CustomException
import web.remember.domain.question.entity.Question
import web.remember.domain.question.entity.QuestionANM
import web.remember.domain.question.entity.QuestionGroup
import web.remember.domain.question.entity.TestType
import web.remember.domain.question.repository.QuestionRepository

@Service
class CareerServiceImpl(
    private val questionRepository: QuestionRepository,
) : CareerService {
    override fun makeQuestion(): Map<String, Map<String, String>> {
        val checkMap =
            mutableMapOf<String, MutableMap<String, String>>().apply {
                QuestionGroup.entries.forEach {
                    if (it.name.startsWith("CAREER")) {
                        put(it.name, mutableMapOf())
                    }
                }
            }
        val questionSet = mutableSetOf<String>()
        val questions: List<Question> = questionRepository.findByType(TestType.CAREER)

        val abilityQuestions = makeAbilityQuestions(questions)
        abilityQuestions.forEach {
            checkMap[it.group.name]!![it.id] = it.content
            questionSet.add(it.id)
        }
        val networkPowerQuestions = makeNetworkPowerQuestions(questions)
        networkPowerQuestions.forEach {
            checkMap[it.group.name]!![it.id] = it.content
            questionSet.add(it.id)
        }
        val moveOnQuestions = makeMoveOnQuestions(questions)
        moveOnQuestions.forEach {
            checkMap[it.group.name]!![it.id] = it.content
            questionSet.add(it.id)
        }
        val numberOfQuestionToAdd = 80 - checkMap.values.size
        for ((key, value) in checkMap) {
            val add = 5 - value.size
            if (add > 0) {
                val filtered =
                    questions.filter { it.group.name == key && !questionSet.contains(it.id) }.shuffled().take(add)
                filtered.forEach {
                    checkMap[key]!![it.id] = it.content
                }
            }
        }
        if (numberOfQuestionToAdd > 0) {
            val filtered =
                questions.filter { !questionSet.contains(it.id) }.shuffled().take(numberOfQuestionToAdd)
            filtered.forEach {
                checkMap[it.group.name]!![it.id] = it.content
            }
        }
        if (checkMap.values.size != 80) {
            throw CustomException("질문 생성에 실패했습니다. 다시 시도해주세요")
        }
        return checkMap
    }

    @Suppress("ktlint:standard:property-naming")
    private fun makeMoveOnQuestions(questions: List<Question>): List<Question> {
        val moveOnQuestions = questions.filter { it.anms?.contains(QuestionANM.MOVE_ON) == true }

        val 성과평가 = moveOnQuestions.filter { it.group == QuestionGroup.CAREER_성과평가 }
        val filtered성과평가 = filter(1, 0, 0, 성과평가)

        val 전문분야 = moveOnQuestions.filter { it.group == QuestionGroup.CAREER_전문분야 }
        val filtered전문분야 = filter(1, 0, 0, 전문분야)

        val 영역확장 = moveOnQuestions.filter { it.group == QuestionGroup.CAREER_영역확장 }
        val filtered영역확장 = filter(2, 0, 0, 영역확장)

        val 네트워크 = moveOnQuestions.filter { it.group == QuestionGroup.CAREER_네트워크 }
        val filtered네트워크 = filter(1, 0, 0, 네트워크)

        val 지원개발 = moveOnQuestions.filter { it.group == QuestionGroup.CAREER_지원개발 }
        val filtered지원개발 = filter(1, 0, 0, 지원개발)

        val why = moveOnQuestions.filter { it.group == QuestionGroup.CAREER_WHY }
        val filteredWhy = filter(2, 0, 0, why)

        val 실천 = moveOnQuestions.filter { it.group == QuestionGroup.CAREER_실천 }
        val filtered실천 = filter(1, 0, 0, 실천)

        val list = mutableListOf<Question>()
        list.addAll(filtered성과평가)
        list.addAll(filtered전문분야)
        list.addAll(filtered영역확장)
        list.addAll(filtered네트워크)
        list.addAll(filtered지원개발)
        list.addAll(filteredWhy)
        list.addAll(filtered실천)
        return list
    }

    @Suppress("ktlint:standard:property-naming")
    private fun makeNetworkPowerQuestions(questions: List<Question>): List<Question> {
        val networkPowerQuestions = questions.filter { it.anms?.contains(QuestionANM.NETWORK_POWER) == true }
        val 성과평가 = networkPowerQuestions.filter { it.group == QuestionGroup.CAREER_성과평가 }
        val filtered성과평가 = filter(0, 2, 0, 성과평가)

        val 조직 = networkPowerQuestions.filter { it.group == QuestionGroup.CAREER_조직 }
        val filtered조직 = filter(1, 1, 0, 조직)

        val 영역개발 = networkPowerQuestions.filter { it.group == QuestionGroup.CAREER_영역개발 }
        val filtered영역개발 = filter(1, 1, 0, 영역개발)

        val 네트워크 = networkPowerQuestions.filter { it.group == QuestionGroup.CAREER_네트워크 }
        val filtered네트워크 = filter(2, 0, 1, 네트워크)

        val 지원개발 = networkPowerQuestions.filter { it.group == QuestionGroup.CAREER_지원개발 }
        val filtered지원개발 = filter(2, 0, 0, 지원개발)

        val 학습계획 = networkPowerQuestions.filter { it.group == QuestionGroup.CAREER_학습계획 }
        val filtered학습계획 = filter(1, 0, 0, 학습계획)

        val 실천 = networkPowerQuestions.filter { it.group == QuestionGroup.CAREER_실천 }
        val filtered실천 = filter(2, 0, 0, 실천)

        val 전문분야 = networkPowerQuestions.filter { it.group == QuestionGroup.CAREER_전문분야 }
        val filtered전문분야 = filter(1, 0, 0, 전문분야)

        val how = networkPowerQuestions.filter { it.group == QuestionGroup.CAREER_HOW }
        val filteredHow = filter(3, 0, 0, how)

        val 업계 = networkPowerQuestions.filter { it.group == QuestionGroup.CAREER_업계 }
        val filtered업계 = filter(1, 0, 0, 업계)

        val list = mutableListOf<Question>()
        list.addAll(filtered성과평가)
        list.addAll(filtered조직)
        list.addAll(filtered영역개발)
        list.addAll(filtered네트워크)
        list.addAll(filtered지원개발)
        list.addAll(filtered학습계획)
        list.addAll(filtered실천)
        list.addAll(filtered전문분야)
        list.addAll(filteredHow)
        list.addAll(filtered업계)
        return list
    }

    @Suppress("ktlint:standard:property-naming")
    private fun makeAbilityQuestions(questions: List<Question>): List<Question> {
        val abilityQuestions = questions.filter { it.anms?.contains(QuestionANM.ABILITY) == true }
        val 영역확장 = abilityQuestions.filter { it.group == QuestionGroup.CAREER_영역확장 }
        val filtered영역확장 = filter(2, 1, 0, 영역확장)

        val 성과평가 = abilityQuestions.filter { it.group == QuestionGroup.CAREER_성과평가 }
        val filtered성과평가 = filter(1, 0, 0, 성과평가)

        val why = abilityQuestions.filter { it.group == QuestionGroup.CAREER_WHY }
        val filteredWhy = filter(1, 0, 1, why)

        val 영역개발 = abilityQuestions.filter { it.group == QuestionGroup.CAREER_영역개발 }
        val filtered영역개발 = filter(1, 0, 0, 영역개발)

        val 지원개발 = abilityQuestions.filter { it.group == QuestionGroup.CAREER_지원개발 }
        val filtered지원개발 = filter(3, 0, 0, 지원개발)

        val 실천 = abilityQuestions.filter { it.group == QuestionGroup.CAREER_실천 }
        val filtered실천 = filter(1, 0, 0, 실천)

        val 월드지수 = abilityQuestions.filter { it.group == QuestionGroup.CAREER_월드지수 }
        val filtered월드지수 = filter(2, 0, 0, 월드지수)

        val how = abilityQuestions.filter { it.group == QuestionGroup.CAREER_HOW }
        val filteredHow = filter(1, 0, 0, how)

        val 업계 = abilityQuestions.filter { it.group == QuestionGroup.CAREER_업계 }
        val filtered업계 = filter(1, 0, 0, 업계)

        val list = mutableListOf<Question>()
        list.addAll(filtered영역확장)
        list.addAll(filtered성과평가)
        list.addAll(filteredWhy)
        list.addAll(filtered영역개발)
        list.addAll(filtered지원개발)
        list.addAll(filtered실천)
        list.addAll(filtered월드지수)
        list.addAll(filteredHow)
        list.addAll(filtered업계)
        return list
    }

    private fun filter(
        ones: Int,
        twos: Int,
        threes: Int,
        questions: List<Question>,
    ): List<Question> {
        val list = mutableListOf<Question>()
        if (ones > 0) list.addAll(questions.filter { it.score == ones }.shuffled().take(ones))
        if (twos > 0) list.addAll(questions.filter { it.score == twos }.shuffled().take(twos))
        if (threes > 0) list.addAll(questions.filter { it.score == threes }.shuffled().take(threes))
        return list
    }
}
