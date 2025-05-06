package web.remember.configuration.filter

import io.jsonwebtoken.Jwts
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * OncePerRequestFilter: HTTP 요청당 한 번만 실행되는 필터
 */
@Component
class JwtAuthenticationFilter : OncePerRequestFilter() {
    @Value("\${jwt.secret}")
    private lateinit var secretKey: String

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authHeader = request.getHeader("Authorization")
        val jwtFromCookie = request.cookies?.firstOrNull { it.name == "jwt" }?.value

        val token =
            when {
                authHeader != null && authHeader.startsWith("Bearer ") -> authHeader.substring(7)
                jwtFromCookie != null -> jwtFromCookie
                else -> null
            }
        if (token != null) {
            try {
                val claims =
                    Jwts
                        .parserBuilder()
                        .setSigningKey(secretKey.toByteArray())
                        .build()
                        .parseClaimsJws(token)
                        .body

                val username = claims.subject
                val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
                val user =
                    org.springframework.security.core.userdetails
                        .User(username, "", authorities)
                val auth = UsernamePasswordAuthenticationToken(user, null, user.authorities)

                SecurityContextHolder.getContext().authentication = auth
            } catch (e: Exception) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token")
                return
            }
        }
        filterChain.doFilter(request, response)
    }
}
