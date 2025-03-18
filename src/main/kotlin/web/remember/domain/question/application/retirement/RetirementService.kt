package web.remember.domain.question.application.retirement

import org.springframework.stereotype.Service

@Service
interface RetirementService {
    fun makeQuestion(): Map<String, Int>
}
