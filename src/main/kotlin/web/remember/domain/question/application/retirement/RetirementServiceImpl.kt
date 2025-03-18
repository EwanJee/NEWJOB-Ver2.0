package web.remember.domain.question.application.retirement

import web.remember.domain.question.repository.QuestionRepository

class RetirementServiceImpl(
    private val questionRepository: QuestionRepository,
) : RetirementService {
    override fun makeQuestion(): Map<String, Int> {
        TODO("Not yet implemented")
    }
}
