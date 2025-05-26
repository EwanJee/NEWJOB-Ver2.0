package web.remember.domain.pdf.worker

import com.rabbitmq.client.Channel
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import web.remember.domain.error.CustomException
import web.remember.domain.gpt.GptService
import web.remember.domain.member.application.MemberService
import web.remember.domain.pdf.application.PdfService
import web.remember.domain.test.application.TestService
import web.remember.domain.test.application.career.CareerTestService
import web.remember.domain.test.application.s3.S3Service

@Service
class PdfWorker(
    private val memberService: MemberService,
    private val careerTestService: CareerTestService,
    private val gptService: GptService,
    private val pdfService: PdfService,
    private val s3Service: S3Service,
    private val testService: TestService,
    private val redisTemplate: RedisTemplate<String, String>,
) {
    @RabbitListener(queues = ["pdf.queue"])
    fun receivePdfId(
        message: Message,
        channel: Channel,
    ) {
//        val deliveryTag = message.messageProperties.deliveryTag
        val raw = String(message.body)
        try {
            val pdfId = raw.split("_").last()
            val (memberId, testId) = pdfId.split(":")

            val dto = memberService.findNameAndIndustryById(memberId)
            val data = careerTestService.findDataById(testId)
            val response = gptService.getGptResponse(data.toString())
            val pdf = pdfService.makeCareerPdf(dto.name, dto.industry, data, response)
            val fileName = "CareerTest_${memberId}_$testId.pdf"
            val pdfUrl = s3Service.uploadFile(pdf, fileName)

            testService.updateUrl(testId, pdfUrl)
            redisTemplate.opsForValue().set("pdf:$raw", pdfUrl)
        } catch (e: Exception) {
            throw CustomException("PDF 생성 중 오류가 발생했습니다.$e \n ${e.message}")
        }
    }
}
