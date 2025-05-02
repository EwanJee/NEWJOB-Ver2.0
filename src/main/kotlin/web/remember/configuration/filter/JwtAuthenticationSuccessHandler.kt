package web.remember.configuration.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import web.remember.domain.member.entity.Member
import web.remember.domain.member.repository.MemberRepository
import web.remember.util.JwtUtil

@Component
class JwtAuthenticationSuccessHandler(
    private val jwtUtil: JwtUtil,
    private val memberRepository: MemberRepository,
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
            memberRepository.save(member)
        }

        val jwt = jwtUtil.generateToken(kakaoId.toString(), claims)
        response.sendRedirect("/oauth2?token=$jwt")
    }
}
