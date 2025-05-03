package web.remember.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import web.remember.util.JwtUtil

@Configuration
class JwtConfig {
    @Value("\${jwt.secret}")
    private lateinit var secretKey: String

    @Bean
    fun jwtUtil(): JwtUtil = JwtUtil(secretKey)
}
