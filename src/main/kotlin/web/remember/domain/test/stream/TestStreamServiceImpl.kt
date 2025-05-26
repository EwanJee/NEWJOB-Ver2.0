package web.remember.domain.test.stream

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.scheduling.annotation.Async
import org.springframework.security.core.context.SecurityContext
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@Service
class TestStreamServiceImpl(
    private val objectMapper: ObjectMapper,
    private val redisTemplate: RedisTemplate<String, String>,
) : TestStreamService {
    @Async
    override fun emit(
        emitter: SseEmitter,
        pdfId: String,
        context: SecurityContext,
    ) {
        try {
            while (true) {
                val status = redisTemplate.opsForValue().get("pdf:$pdfId")
                val jsonData = objectMapper.writeValueAsString(mapOf("status" to status, "url" to status))
                emitter.send(
                    SseEmitter
                        .event()
                        .name("message")
                        .data(jsonData)
                        .reconnectTime(0),
                )

                if (status != "IN_PROGRESS") {
                    emitter.complete()
                    redisTemplate.delete("pdf:$pdfId")
                    break
                }

                Thread.sleep(2000)
            }
        } catch (e: Exception) {
            emitter.completeWithError(e)
        } finally {
//            SecurityContextHolder.clearContext()
        }
    }
}
