package web.remember.domain.gpt

import RequestGpt
import com.fasterxml.jackson.databind.ObjectMapper
import com.nasa.todaynasa.application.service.apod.request.gpt.RequestAnswerGpt
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import web.remember.aop.MeasureExecutionTime
import web.remember.domain.error.CustomException

@Service
class GptServiceImpl(
    @Qualifier("gptClient")
    private val gptClient: WebClient,
    private val objectMapper: ObjectMapper,
) : GptService {
    @MeasureExecutionTime
    override fun getGptResponse(requestBody: String): String {
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
//                .awaitBody<RequestAnswerGpt>()
                .bodyToMono(RequestAnswerGpt::class.java)
                .block() // 블로킹 방식으로 응답을 기다림
        val content =
            response
                ?.choices
                ?.get(0)
                ?.message
                ?.content ?: throw CustomException("GPT 응답을 받을 수 없습니다.")
        return content
    }
}
