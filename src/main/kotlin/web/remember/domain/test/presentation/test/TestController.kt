package web.remember.domain.test.presentation.test

import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import web.remember.domain.test.application.TestService
import web.remember.domain.test.dto.ResponseTestDto
import web.remember.domain.test.presentation.test.dto.PageResponse
import web.remember.util.JwtUtil

@RestController
@RequestMapping("/api/v1/tests")
class TestController(
    private val testService: TestService,
    private val jwtUtil: JwtUtil,
) {
    @GetMapping()
    fun findAll(
        @CookieValue("jwt") jwt: String,
        @RequestParam("page") page: Int,
    ): ResponseEntity<PageResponse<ResponseTestDto>> {
        val claims = jwtUtil.parseClaims(jwt)
        val memberId = claims["memberId"] as String
        val page: Page<ResponseTestDto> = testService.findAllByMemberId(memberId, page)
        return ResponseEntity.ok(
            PageResponse(
                content = page.content,
                page = page.number,
                size = page.size,
                totalElements = page.totalElements,
                totalPages = page.totalPages,
                isLast = page.isLast,
            ),
        )
    }
}
