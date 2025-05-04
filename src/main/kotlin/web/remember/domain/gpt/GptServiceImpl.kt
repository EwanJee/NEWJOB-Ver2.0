package web.remember.domain.gpt

import RequestGpt
import com.fasterxml.jackson.databind.ObjectMapper
import com.nasa.todaynasa.application.service.apod.request.gpt.RequestAnswerGpt
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import web.remember.domain.error.CustomException

@Service
class GptServiceImpl(
    @Qualifier("gptClient")
    private val gptClient: WebClient,
    private val objectMapper: ObjectMapper,
) : GptService {
    override suspend fun getGptResponse(requestBody: String): String {
        val requestGpt =
            RequestGpt(
                model = "gpt-4.1",
                messages =
                    mutableListOf(
                        Message.initSystem(),
                        Message.initUser(requestBody),
                    ),
            )
        val response =
            gptClient
                .post()
                .bodyValue(requestGpt)
                .retrieve()
                .awaitBody<RequestAnswerGpt>()
        val content =
            response
                .choices[0]
                .message
                .content ?: throw CustomException("GPT 응답을 받을 수 없습니다.")
        return content
    }
}
