package web.remember.domain.test.presentation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import web.remember.domain.test.application.pdf.PdfService

@RestController
@RequestMapping("/api/v1/pdf")
class PdfController(
    private val pdfService: PdfService,
) {
    @PostMapping()
    fun createPdf(
        name: String,
        data: MutableMap<String, String>,
        file: MultipartFile,
    ): ResponseEntity<ByteArray> {
        val pdf: ByteArray = pdfService.makeCareerPdf(name, data, file)
        return ResponseEntity.ok(pdf)
    }
}
