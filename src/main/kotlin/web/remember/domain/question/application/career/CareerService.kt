package web.remember.domain.question.application.career

interface CareerService {
    fun makeQuestion(): Map<String, List<String>>
}
