package web.remember.configuration.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import web.remember.domain.member.entity.Member
import web.remember.domain.member.repository.MemberRepository
import web.remember.util.JwtUtil
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

@Component
class JwtAuthenticationSuccessHandler(
    private val jwtUtil: JwtUtil,
    private val memberRepository: MemberRepository,
    private val authorizedClientService: OAuth2AuthorizedClientService,
    private val redisTemplate: RedisTemplate<String, String>,
) : AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?,
    ) {
        handleAuthenticationSuccess(request!!, response!!, authentication!!)
    }

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authentication: Authentication?,
    ) {
        handleAuthenticationSuccess(request!!, response!!, authentication!!)
        chain?.doFilter(request, response)
    }

    private fun handleAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {
        val user = authentication.principal as OAuth2User
        val oauthToken = authentication as OAuth2AuthenticationToken

        val authorizedClient: OAuth2AuthorizedClient? =
            authorizedClientService.loadAuthorizedClient(
                oauthToken.authorizedClientRegistrationId,
                oauthToken.name,
            )

        val kakaoAccessToken = authorizedClient?.accessToken?.tokenValue
        val kakaoTokenExpiresAt = authorizedClient?.accessToken?.expiresAt
        val kakaoRefreshToken = authorizedClient?.refreshToken?.tokenValue
        val attributes = user.attributes
        val kakaoAccount = attributes["kakao_account"] as? Map<*, *>
        val profile = kakaoAccount?.get("profile") as? Map<*, *>
        val kakaoId = attributes["id"] as Long
        val id = kakaoId

        val email = kakaoAccount?.get("email") as? String ?: ""
        val nickname = profile?.get("nickname") as? String ?: ""

        val claims: MutableMap<String, Any> = mutableMapOf()
        claims["email"] = email
        claims["name"] = nickname

        var member = memberRepository.findByKakaoId(id)

        if (member == null) {
            member =
                Member(
                    name = nickname,
                    phoneNumber = "",
                    industry = "",
                )
            member.kakaoId = id
            member.emailL = email
            val saved = memberRepository.save(member)
        }
        claims["memberId"] = member.id
        claims["kakaoId"] = member.kakaoId
        claims["industry"] = member.industry
        claims["phoneNumber"] = member.phoneNumber
        // redis
        val redisKey = "kakao:token:$kakaoId"
        val value =
            """
            {
                "kakaoAccessToken": "$kakaoAccessToken",
                "kakaoTokenExpiresAt": "$kakaoTokenExpiresAt",
                "kakaoRefreshToken": "$kakaoRefreshToken"
            }
            """.trimIndent()
        val zoneId = ZoneId.systemDefault() // 또는 ZoneId.of("Asia/Seoul") 등 명시적으로 지정
        val expiresAt = (kakaoTokenExpiresAt ?: Instant.now().plus(4, ChronoUnit.HOURS)).atZone(zoneId)
        val ttl = Duration.between(ZonedDateTime.now(zoneId), expiresAt)

        redisTemplate.opsForValue().set(redisKey, value, ttl)
        val jwt = jwtUtil.generateToken(kakaoId.toString(), claims)
        val jwtCookie =
            Cookie("jwt", jwt).apply {
                path = "/" // 모든 경로에서 쿠키 사용
                maxAge = 60 * 60 * 4 // 4시간
                isHttpOnly = true // 자바스크립트 접근 방지 → XSS 보안
                secure = false // HTTPS 환경에서만 전송
            }
        // 응답에 쿠키 추가
        response.addCookie(jwtCookie)
        response.sendRedirect("/login-success")
    }
}
