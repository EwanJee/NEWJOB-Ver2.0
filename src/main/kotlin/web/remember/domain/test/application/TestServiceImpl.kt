package web.remember.domain.test.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import web.remember.domain.error.CustomException
import web.remember.domain.test.repository.TestRepository

@Service
class TestServiceImpl(
    private val testRepository: TestRepository,
) : TestService {
    @Transactional
    override fun updateUrl(
        testId: String,
        url: String,
    ): String {
        val test =
            testRepository
                .findById(testId.toLong())
                .orElseThrow { CustomException("테스트를 찾을 수 없습니다.") }
        test.url = url
        return test.url
    }
}
