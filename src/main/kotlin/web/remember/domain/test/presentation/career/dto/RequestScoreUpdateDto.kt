package web.remember.domain.test.presentation.career.dto

class RequestScoreUpdateDto(
    val scoreMap: Map<String, Int>,
) {
    var memberId: String? = null
    var testId: String? = null

    fun update(
        memberId: String,
        testId: String,
    ) {
        this.memberId = memberId
        this.testId = testId
    }
}
