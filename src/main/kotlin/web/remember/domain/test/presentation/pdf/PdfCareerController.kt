@file:Suppress("ktlint:standard:no-wildcard-imports")

package web.remember.domain.test.presentation.pdf

import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import web.remember.domain.error.CustomException
import web.remember.domain.member.application.MemberService
import web.remember.domain.test.application.career.CareerTestService
import web.remember.domain.test.application.pdf.PdfService
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
) {
    @PostMapping("/create", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createPdf(
        @CookieValue("jwt") jwt: String?,
        @RequestPart("file") file: MultipartFile,
        httpSession: HttpSession,
    ): ResponseEntity<String> {
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

        val dto = memberService.findNameAndIndustryById(memberId)
        val data = careerTestService.findDataById(testId)
        val pdf: ByteArray = pdfService.makeCareerPdf(dto.name, dto.industry, data, file)
        val fileName = "CareerTest_${memberId}_$testId.pdf"
        val pdfUrl = s3Service.uploadFile(pdf, fileName) // S3에 업로드
        return ResponseEntity.ok().body(pdfUrl)
    }

    @GetMapping("")
    fun viewPdf(
        @SessionAttribute("pdf", required = false) pdf: ByteArray?,
    ): ResponseEntity<ByteArray> {
        if (pdf == null) {
            throw CustomException("PDF 파일이 없습니다.")
        }
        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf)
    }

    @PostMapping("/test", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun test(
        @RequestPart("file") file: MultipartFile,
    ): ResponseEntity<ByteArray> {
        val pdf: ByteArray = pdfService.test(file)
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf)
    }
}
