package web.remember.domain.test.presentation.pdf

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
interface PdfService {
    fun makeCareerPdf(
        name: String,
        data: MutableMap<String, String>,
        pdf: MultipartFile,
    ): ByteArray
}
