package web.remember.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import web.remember.configuration.filter.AuthenticationInterceptor

@Configuration
class WebConfig(
    private val authenticationInterceptor: AuthenticationInterceptor,
) : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry
            .addMapping("/**")
            .allowedOrigins("http://localhost:8080") // 프론트엔드 URL
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry
            .addInterceptor(authenticationInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns(
                "/assets/**",
                "/css/**",
                "/js/**",
                "/images/**",
                "/api/**",
                "/error",
                "/favicon.ico",
            )
    }
}
