@file:Suppress("ktlint:standard:no-wildcard-imports")

package web.remember.domain.test.presentation.career

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import web.remember.domain.error.CustomException
import web.remember.domain.member.application.MemberService
import web.remember.domain.test.application.career.CareerTestService
import web.remember.domain.test.presentation.career.dto.RequestCreateCareerTestDto
import web.remember.domain.test.presentation.career.dto.RequestScoreUpdateDto
import web.remember.domain.test.presentation.career.dto.RequestUpdateCareerAge
import web.remember.util.JwtUtil

@RestController
@RequestMapping("/api/v1/career-test")
class CareerTestController(
    private val careerTestService: CareerTestService,
    private val memberService: MemberService,
    private val jwtUtil: JwtUtil,
) {
    @PostMapping()
    fun startTest(
        @RequestBody @Valid dto: RequestCreateCareerTestDto,
        @CookieValue("jwt") jwt: String?,
        response: HttpServletResponse,
    ): ResponseEntity<String> {
        if (jwt == null) {
            throw CustomException("로그인이 필요합니다.")
        }
        val claims = jwtUtil.parseClaims(jwt).toMutableMap()
        val memberId = claims["memberId"] as String? ?: throw CustomException("로그인이 필요합니다.")
        if (!memberService.existsById(memberId)) {
            throw CustomException("존재하지 않는 회원입니다")
        }
        val testId = careerTestService.startTest(dto.questionMap, memberId)
        claims["testId"] = testId
        val newJwt = jwtUtil.generateToken(memberId, claims)
        val newJwtCookie =
            Cookie("jwt", newJwt).apply {
                isHttpOnly = true
                maxAge = 60 * 60 * 4 // 1 day
                path = "/"
            }
        response.addCookie(newJwtCookie)
        return ResponseEntity.ok(testId)
    }

    @PutMapping("/score")
    fun updateScore(
        @RequestBody @Valid dto: RequestScoreUpdateDto,
        @CookieValue("jwt") jwt: String?,
    ) {
        if (jwt == null) {
            throw CustomException("로그인이 필요합니다.")
        }
        val claims = jwtUtil.parseClaims(jwt).toMutableMap()
        val memberId = claims["memberId"] as String? ?: throw CustomException("로그인이 필요합니다.")
        val testId = claims["testId"] as String? ?: throw CustomException("로그인이 필요합니다.")
        dto.update(memberId, testId)
        careerTestService.updateScore(dto)
    }

    @PutMapping("/age")
    fun updateCareerAge(
        @RequestBody @Valid dto: RequestUpdateCareerAge,
        @CookieValue("jwt") jwt: String?,
    ) {
        if (jwt == null) {
            throw CustomException("로그인이 필요합니다.")
        }
        val claims = jwtUtil.parseClaims(jwt).toMutableMap()
        val memberId = claims["memberId"] as String? ?: throw CustomException("로그인이 필요합니다.")
        val testId = claims["testId"] as String? ?: throw CustomException("로그인이 필요합니다.")
        careerTestService.updateCareerAge(testId, memberId, dto.age)
    }

    @PutMapping("/finish")
    fun finishTest(
        @CookieValue("jwt") jwt: String?,
    ) {
        if (jwt == null) {
            throw CustomException("로그인이 필요합니다.")
        }
        val claims = jwtUtil.parseClaims(jwt).toMutableMap()
        val memberId = claims["memberId"] as String? ?: throw CustomException("로그인이 필요합니다.")
        val testId = claims["testId"] as String? ?: throw CustomException("로그인이 필요합니다.")
        careerTestService.finishTest(memberId, testId)
    }
}
