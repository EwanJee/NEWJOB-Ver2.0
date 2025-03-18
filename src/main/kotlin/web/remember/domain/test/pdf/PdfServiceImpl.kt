package web.remember.domain.test.pdf

import com.itextpdf.forms.PdfAcroForm
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.font.PdfFont
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import org.springframework.web.multipart.MultipartFile
import web.remember.domain.question.entity.QuestionGroup
import java.io.ByteArrayInputStream

class PdfServiceImpl : PdfService {
    override fun makeCareerPdf(
        name: String,
        data: MutableMap<String, String>,
        pdf: MultipartFile,
    ): ByteArray {
        val inputStream = ByteArrayInputStream(pdf.bytes)
        val reader = PdfReader(inputStream)
        val outputStream: ByteArrayOutputStream = ByteArrayOutputStream()
        val writer: PdfWriter = PdfWriter(outputStream)
        val pdfDoc = PdfDocument(reader, writer)

        val canvas1: PdfCanvas = PdfCanvas(pdfDoc.firstPage)
        val canvas2: PdfCanvas = PdfCanvas(pdfDoc.getPage(2))
        val canvas3: PdfCanvas = PdfCanvas(pdfDoc.getPage(3))
        val font: PdfFont = canvas1.document.defaultFont

        // CHARACTER : 3.2, 4.02
        // CONNECTION : 3.2, 5.5
        // CONDITION : 3.2, 6.87
        // CHALLENGE : 3.2, 8.32
        // CONTROL : 3.2, 9.75
        // loc 1.85, 4.23

        val form = PdfAcroForm.getAcroForm(pdfDoc, true)

        QuestionGroup.entries.forEach {
            val score = data[it.name]
            when (it.name) {
                QuestionGroup.CAREER_WHY.toString() -> {
                    form.getField("WHY").setValue(score)
                }
            }
        }

        pdfDoc.close()

        val pdfBytes = outputStream.toByteArray()
        return pdfBytes
    }

    private fun convertToPoint(
        x: Double,
        y: Double,
    ): DoubleArray {
        val xPoint = x * 72
        val yPoint = y * 72
        return doubleArrayOf(xPoint, yPoint)
    }
}
