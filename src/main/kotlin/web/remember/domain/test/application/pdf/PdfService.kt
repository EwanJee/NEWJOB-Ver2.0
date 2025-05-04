package web.remember.domain.test.application.pdf

import org.springframework.web.multipart.MultipartFile

interface PdfService {
    fun makeCareerPdf(
        name: String,
        industry: String,
        data: MutableMap<String, String>,
        response: String,
    ): ByteArray

    fun test(pdf: MultipartFile): ByteArray
}
