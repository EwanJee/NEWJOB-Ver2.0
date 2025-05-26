package web.remember.configuration.filter

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import web.remember.util.JwtUtil

@Component
class AuthenticationInterceptor(
    private val jwtUtil: JwtUtil,
) : HandlerInterceptor {
    private val logger = LoggerFactory.getLogger(AuthenticationInterceptor::class.java)

    // 인증이 필요한 페이지들
    private val protectedPaths =
        setOf(
            "/mypage",
            "/career",
            "/career/result",
            "/career/result/payment",
            "/home",
        )

    // 인증이 필요 없는 페이지들
    private val publicPaths =
        setOf(
            "/login",
            "/oauth2",
            "/privacy-policy",
            "/error",
            "/login-success",
        )

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val requestURI = request.requestURI

        // 정적 자원이나 API는 스킵
        if (requestURI.startsWith("/api/") ||
            requestURI.startsWith("/assets/") ||
            requestURI.startsWith("/css/") ||
            requestURI.startsWith("/js/") ||
            requestURI.startsWith("/images/")
        ) {
            return true
        }

        // 공개 페이지는 인증 체크 안함
        if (publicPaths.contains(requestURI)) {
            return true
        }

        // 보호된 페이지인 경우 JWT 체크
        if (protectedPaths.contains(requestURI)) {
            val jwtCookie = request.cookies?.find { it.name == "jwt" }?.value

            if (jwtCookie == null) {
                response.sendRedirect("/")
                return false
            }

            try {
                jwtUtil.parseClaims(jwtCookie)
                return true
            } catch (e: Exception) {
                response.sendRedirect("/")
                return false
            }
        }

        return true
    }
}
