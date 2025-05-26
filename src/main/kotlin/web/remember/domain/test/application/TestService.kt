package web.remember.domain.test.application

import org.springframework.data.domain.Page
import web.remember.domain.test.dto.ResponseTestDto

interface TestService {
    fun updateUrl(
        testId: String,
        url: String,
    ): String

    fun isValidForPayment(
        testId: Long,
        memberId: String,
    ): Boolean

    fun findAll(page: Int): Page<ResponseTestDto>

    fun findAllByMemberId(
        memberId: String,
        page: Int,
    ): Page<ResponseTestDto>
}
