package web.remember.domain.question.application.retirement

interface RetirementService {
    fun makeQuestion(): Map<String, Int>
}
