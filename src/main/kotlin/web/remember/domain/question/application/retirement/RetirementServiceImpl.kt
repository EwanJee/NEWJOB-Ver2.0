package web.remember.domain.question.application.retirement

import org.springframework.stereotype.Service
import web.remember.domain.question.repository.QuestionRepository

@Service
class RetirementServiceImpl(
    private val questionRepository: QuestionRepository,
) : RetirementService {
    override fun makeQuestion(): Map<String, Int> {
        TODO("Not yet implemented")
    }
}
