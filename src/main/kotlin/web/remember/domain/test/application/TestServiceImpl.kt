package web.remember.domain.test.application

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import web.remember.domain.error.CustomException
import web.remember.domain.test.dto.ResponseTestDto
import web.remember.domain.test.entity.Test
import web.remember.domain.test.repository.TestRepository
import java.time.LocalDateTime

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

    override fun isValidForPayment(
        testId: Long,
        memberId: String,
    ): Boolean =
        testRepository.findPaidByIdAndMemberId(testId, memberId)
            ?: throw CustomException("결제 정보가 없습니다.")

    override fun findAll(page: Int): Page<ResponseTestDto> {
        val pageable: Pageable = PageRequest.of(page, 3)
        val testPage: Page<Test> = testRepository.findAll(pageable)
        return testPage.map { test ->
            val testId =
                test.id
                    ?: throw CustomException("테스트를 찾을 수 없습니다.")
            ResponseTestDto(
                id = testId,
                name = test.testType.name,
                url = test.url,
                paid = test.paid,
                createdAt = test.createdAt ?: LocalDateTime.now(),
            )
        }
    }

    override fun findAllByMemberId(
        memberId: String,
        page: Int,
    ): Page<ResponseTestDto> {
        val pageable: Pageable = PageRequest.of(page - 1, 3, Sort.by("createdAt").descending())
        val testPage: Page<Test> = testRepository.findByMemberId(memberId, pageable)
        return testPage.map { test ->
            val testId =
                test.id
                    ?: throw CustomException("테스트를 찾을 수 없습니다.")
            ResponseTestDto(
                id = testId,
                name = test.testType.name,
                url = test.url ?: "",
                paid = test.paid,
                createdAt = test.createdAt ?: LocalDateTime.now(),
            )
        }
    }
}
