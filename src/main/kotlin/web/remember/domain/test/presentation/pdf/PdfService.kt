package web.remember.domain.test.presentation.pdf

import org.springframework.web.multipart.MultipartFile

interface PdfService {
    fun makeCareerPdf(
        name: String,
        data: MutableMap<String, String>,
        pdf: MultipartFile,
    ): ByteArray
}
