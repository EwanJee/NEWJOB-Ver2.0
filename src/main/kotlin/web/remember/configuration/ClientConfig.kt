package web.remember.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class ClientConfig {
    @Bean
    fun webClient(): WebClient.Builder = WebClient.builder()
}
