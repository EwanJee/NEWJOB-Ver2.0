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

@RestController
@RequestMapping("/api/v1/pdf/career")
class PdfCareerController(
    private val pdfService: PdfService,
    private val memberService: MemberService,
    private val careerTestService: CareerTestService,
) {
    @PostMapping("/create", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createPdf(
        @SessionAttribute("memberId", required = false) memberId: String?,
        @SessionAttribute("testId", required = false) testId: String?,
        @RequestPart("file") file: MultipartFile,
        httpSession: HttpSession,
    ): ResponseEntity<ByteArray> {
        if (memberId == null || testId == null) {
            throw CustomException("세션 정보가 없습니다. 로그인 후, 다시 실행해주세요")
        }

        val dto = memberService.findNameAndIndustryById(memberId)
        val data = careerTestService.findDataById(testId)
        val pdf: ByteArray = pdfService.makeCareerPdf(dto.name, dto.industry, data, file)
        httpSession.setAttribute("pdf", pdf)
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf)
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
