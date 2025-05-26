@file:Suppress("ktlint:standard:no-wildcard-imports")

package web.remember.domain.pdf.presentation

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import web.remember.domain.error.CustomException
import web.remember.domain.gpt.GptService
import web.remember.domain.member.application.MemberService
import web.remember.domain.pdf.application.PdfService
import web.remember.domain.pdf.application.RabbitService
import web.remember.domain.test.application.TestService
import web.remember.domain.test.application.career.CareerTestService
import web.remember.domain.test.application.s3.S3Service
import web.remember.util.JwtUtil

@RestController
@RequestMapping("/api/v1/pdf/career")
class PdfCareerController(
    private val pdfService: PdfService,
    private val memberService: MemberService,
    private val careerTestService: CareerTestService,
    private val s3Service: S3Service,
    private val jwtUtil: JwtUtil,
    private val testService: TestService,
    private val gptService: GptService,
    private val rabbitService: RabbitService,
) {
    @PostMapping("/create")
    fun createPdf(
        @CookieValue("jwt") jwt: String?,
    ): ResponseEntity<Map<String, String>> {
        if (jwt == null) {
            throw CustomException("로그인이 필요합니다.")
        }
        val claims = jwtUtil.parseClaims(jwt).toMutableMap()
        val memberId =
            claims["memberId"] as String?
                ?: throw CustomException("세션 정보가 없습니다. 로그인 후, 다시 실행해주세요")
        val testId =
            claims["testId"] as String?
                ?: throw CustomException("세션 정보가 없습니다. 로그인 후, 다시 실행해주세요")
//
//        val dto: ResponseNameAndIndustry = memberService.findNameAndIndustryById(memberId)
//        val data: MutableMap<String, String> = careerTestService.findDataById(testId)
//        val response = gptService.getGptResponse(data.toString())
//        val pdf: ByteArray = pdfService.makeCareerPdf(dto.name, dto.industry, data, response)
//        val fileName = "CareerTest_${memberId}_$testId.pdf"
//        val pdfUrl = s3Service.uploadFile(pdf, fileName) // S3에 업로드
//        testService.updateUrl(testId, pdfUrl) // DB에 URL 저장
        val pdfId: String = "CareerTest_$memberId:$testId"
        val response = rabbitService.sendPdfIdToQueue(pdfId)
        return ResponseEntity.ok().body(mapOf("testId" to testId))
    }

    @GetMapping("/status/{testId}", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getPdfStatus(
        @PathVariable testId: String,
        @CookieValue("jwt") jwt: String?,
    ): ResponseEntity<SseEmitter> {
        if (jwt == null) {
            throw CustomException("로그인이 필요합니다.")
        }
        val claims = jwtUtil.parseClaims(jwt).toMutableMap()
        val memberId =
            claims["memberId"] as String?
                ?: throw CustomException("세션 정보가 없습니다. 로그인 후, 다시 실행해주세요")
        val pdfId: String = "CareerTest_$memberId:$testId"
        val emitter = SseEmitter(60_000L)
        return ResponseEntity.ok().body(emitter)
    }
}
