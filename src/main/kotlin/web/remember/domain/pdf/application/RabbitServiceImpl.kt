package web.remember.domain.pdf.application

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class RabbitServiceImpl(
    private val redisTemplate: RedisTemplate<String, String>,
    private val rabbitTemplate: RabbitTemplate,
) : RabbitService {
    override fun sendPdfIdToQueue(pdfId: String): String {
        // Redis에 PDF ID 저장
        redisTemplate.opsForValue().set("pdf:$pdfId", "IN_PROGRESS")

        // RabbitMQ에 PDF ID 전송
        rabbitTemplate.convertAndSend("pdf.exchange", "pdf.create", pdfId)

        return "PDF ID $pdfId 가 RabbitMQ에 전송되었습니다."
    }
}
