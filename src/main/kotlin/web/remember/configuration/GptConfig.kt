package web.remember.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration

@Configuration
class GptConfig {
    private val gptUrl: String = "https://api.openai.com/v1/chat/completions"

    @Value("\${gpt.key}")
    private lateinit var gptKey: String

    @Bean("gptClient")
    fun gptClient(): WebClient {
        val client = HttpClient.create().responseTimeout(Duration.ofSeconds(45))
        return WebClient
            .builder()
            .clientConnector(ReactorClientHttpConnector(client))
            .baseUrl(gptUrl)
            .defaultHeader("Content-Type", "application/json")
            .defaultHeader("Authorization", "Bearer $gptKey")
            .build()
    }
}
