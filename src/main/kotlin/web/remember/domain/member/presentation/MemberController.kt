@file:Suppress("ktlint:standard:no-wildcard-imports")

package web.remember.domain.member.presentation

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.slf4j.Logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import web.remember.domain.member.application.MemberRedisService
import web.remember.domain.member.application.MemberService
import web.remember.domain.member.dto.RequestCreateMemberDto
import web.remember.util.JwtUtil
import web.remember.util.KakaoUtil

@RequestMapping("/api/v1/members")
@RestController
class MemberController(
    private val memberService: MemberService,
    private val kakaoUtil: KakaoUtil,
    private val jwtUtil: JwtUtil,
    private val memberRedisService: MemberRedisService,
) {
    val logger: Logger = org.slf4j.LoggerFactory.getLogger(MemberController::class.java)

    @GetMapping("/me")
    fun getMe(
        @CookieValue("jwt") jwt: String,
    ): ResponseEntity<Map<String, String>> {
        val claims: MutableMap<String, Any> = jwtUtil.parseClaims(jwt)
        val imageUrl = claims["image"] as String
        val name = claims["name"] as String
        if (claims["kakaoId"] == null || claims["memberId"] == null) {
            return ResponseEntity.badRequest().build()
        }
        return ResponseEntity.ok(
            mapOf(
                "image" to imageUrl,
                "name" to name,
            ),
        )
    }

    @GetMapping("/logout")
    fun logout(
        @CookieValue("jwt") jwt: String,
        response: HttpServletResponse,
    ): ResponseEntity<Void> {
        val claims: MutableMap<String, Any> = jwtUtil.parseClaims(jwt)
        val kakaoId = claims["kakaoId"] as String
        if (claims["kakaoId"] == null) {
            return ResponseEntity.badRequest().build()
        }
        memberRedisService.evictToken(kakaoId)
        val expiredCookie =
            Cookie("jwt", null).apply {
                maxAge = 0
                path = "/"
            }
        response.addCookie(expiredCookie)
        return ResponseEntity.ok().build()
    }

    @PostMapping()
    fun createMember(
        @RequestBody @Valid memberDto: RequestCreateMemberDto,
    ): ResponseEntity<Void> = ResponseEntity.ok().build()
}
