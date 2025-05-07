package web.remember.domain.test.application

interface TestService {
    fun updateUrl(
        testId: String,
        url: String,
    ): String

    fun isValidForPayment(
        testId: Long,
        memberId: String,
    ): Boolean
}
