package web.remember.configuration.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.ResponseCookie
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import web.remember.domain.error.CustomException
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
    @Value("\${spring.profiles.active}")
    private lateinit var activeProfile: String
    private val logger = org.slf4j.LoggerFactory.getLogger(JwtAuthenticationSuccessHandler::class.java)

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
        val nickname = profile?.get("nickname") as? String ?: throw CustomException("이름을 가져올 수 없습니다.")
        val name = kakaoAccount["name"] as? String ?: throw CustomException("이름을 가져올 수 없습니다.")
        val profileImageUrl = profile["profile_image_url"] as? String ?: ""
        val phoneNumber = kakaoAccount["phone_number"] as? String ?: throw CustomException("전화번호를 가져올 수 없습니다.")
        logger.info("name: $name, email: $email, phoneNumber: $phoneNumber")

        val claims: MutableMap<String, Any> = mutableMapOf()
//        claims["email"] = email
        claims["name"] = name
        claims["image"] = profileImageUrl

        var member = memberRepository.findByKakaoId(id)

        if (member == null) {
            member =
                Member(
                    name = nickname,
                    phoneNumber = normalizeKoreanPhoneNumber(phoneNumber),
                    industry = "",
                )
            member.kakaoId = id
            member.emailL = email
            val saved = memberRepository.save(member)
            member = saved
        }
        claims["memberId"] = member.id
        claims["kakaoId"] = member.kakaoId
//        claims["industry"] = member.industry
//        claims["phoneNumber"] = member.phoneNumber
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
        val zoneId = ZoneId.systemDefault()
        val expiresAt = (kakaoTokenExpiresAt ?: Instant.now().plus(4, ChronoUnit.HOURS)).atZone(zoneId)
        val ttl = Duration.between(ZonedDateTime.now(zoneId), expiresAt)

        redisTemplate.opsForValue().set(redisKey, value, ttl)
        val jwt = jwtUtil.generateToken(kakaoId.toString(), claims)
        val jwtCookie: ResponseCookie =
            if (activeProfile == "dev") {
                createCookieDev(jwt)
            } else {
                createCookieProd(jwt)
            }
        response.addHeader("Set-Cookie", jwtCookie.toString())
        response.sendRedirect("/login-success")
    }

    private fun normalizeKoreanPhoneNumber(number: String): String =
        number
            .replace("+82", "0")
            .replace(Regex("[^0-9]"), "")

    private fun createCookieDev(jwt: String): ResponseCookie =
        ResponseCookie
            .from("jwt", jwt)
            .path("/")
            .httpOnly(true)
            .maxAge(Duration.ofHours(4))
            .sameSite("Lax")
            .secure(false)
            .build()

    private fun createCookieProd(jwt: String): ResponseCookie =
        ResponseCookie
            .from("jwt", jwt)
            .path("/")
            .httpOnly(true)
            .maxAge(Duration.ofHours(4))
            .sameSite("None")
            .secure(true)
            .build()
}
