package web.remember.domain.pdf.application

interface RabbitService {
    fun sendPdfIdToQueue(pdfId: String): String
}
