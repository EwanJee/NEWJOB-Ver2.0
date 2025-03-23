package web.remember.domain.test.application.pdf

import org.springframework.web.multipart.MultipartFile

interface PdfService {
    fun makeCareerPdf(
        name: String,
        data: MutableMap<String, String>,
        pdf: MultipartFile,
    ): ByteArray

    fun test(pdf: MultipartFile): ByteArray
}
