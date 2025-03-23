@file:Suppress("ktlint:standard:no-wildcard-imports")

package web.remember.domain.test.presentation.pdf

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import web.remember.domain.test.application.pdf.PdfService
import web.remember.domain.test.presentation.pdf.dto.RequestCreatePdfDto

@RestController
@RequestMapping("/api/v1/pdf")
class PdfController(
    private val pdfService: PdfService,
) {
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createPdf(
        @RequestBody dto: RequestCreatePdfDto,
        @RequestPart("file") file: MultipartFile,
    ): ResponseEntity<ByteArray> {
        val pdf: ByteArray = pdfService.makeCareerPdf(dto.name, dto.data, file)
        return ResponseEntity.ok(pdf)
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
