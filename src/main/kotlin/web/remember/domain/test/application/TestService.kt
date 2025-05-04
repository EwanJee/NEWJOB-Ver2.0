package web.remember.domain.test.application

interface TestService {
    fun updateUrl(
        testId: String,
        url: String,
    ): String
}
