package web.remember.domain.test.dto

data class RequestScoreUpdateDto(
    val memberId: String,
    val testId: String,
    val scoreMap: Map<String, Int>,
)
