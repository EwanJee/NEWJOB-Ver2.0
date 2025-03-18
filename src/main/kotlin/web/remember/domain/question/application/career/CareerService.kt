package web.remember.domain.question.application.career

import org.springframework.stereotype.Service

@Service
interface CareerService {
    fun makeQuestion(): Map<String, List<String>>
}
