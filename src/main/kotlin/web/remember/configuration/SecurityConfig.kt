package web.remember.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import web.remember.configuration.filter.JwtAuthenticationFilter
import web.remember.configuration.filter.JwtAuthenticationSuccessHandler

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val successHandler: JwtAuthenticationSuccessHandler,
) {
    private val logger = org.slf4j.LoggerFactory.getLogger(SecurityConfig::class.java)

    @Bean
    fun encoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers(
                        "/assets/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/favicon.ico",
                        "/image/**",
                        "/text/**",
                        "/public/**",
                        "/error/**",
                    ).permitAll()
                    .requestMatchers("/api/v1/**")
                    .authenticated()
                    .anyRequest()
                    .permitAll()
            }.oauth2Login {
                it.successHandler(successHandler)
            }.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 비활성화
            }
        return http.build()
    }
}
