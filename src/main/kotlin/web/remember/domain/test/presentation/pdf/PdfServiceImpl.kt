package web.remember.domain.test.presentation.pdf

import com.itextpdf.forms.PdfAcroForm
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.PdfWriter
import org.springframework.web.multipart.MultipartFile
import web.remember.domain.question.entity.QuestionANM
import web.remember.domain.question.entity.QuestionCtype
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

//        val canvas1: PdfCanvas = PdfCanvas(pdfDoc.firstPage)
//        val canvas2: PdfCanvas = PdfCanvas(pdfDoc.getPage(2))
//        val canvas3: PdfCanvas = PdfCanvas(pdfDoc.getPage(3))
//        val font: PdfFont = canvas1.document.defaultFont
        val form = PdfAcroForm.getAcroForm(pdfDoc, true)

        QuestionGroup.entries.forEach {
            val score = data[it.name]
            when (it.name) {
                QuestionGroup.CAREER_WHY.toString() -> {
                    form.getField("WHY").setValue(score)
                }

                QuestionGroup.CAREER_HOW.toString() -> {
                    form.getField("HOW").setValue(score)
                }

                QuestionGroup.CAREER_WHAT.toString() -> {
                    form.getField("WHAT").setValue(score)
                }

                QuestionGroup.CAREER_성과평가.toString() -> {
                    form.getField("SUCCESS").setValue(score)
                }

                QuestionGroup.CAREER_네트워크.toString() -> {
                    form.getField("NETWORK").setValue(score)
                }

                QuestionGroup.CAREER_팀웍동료.toString() -> {
                    form.getField("TEAMWORK").setValue(score)
                }

                QuestionGroup.CAREER_조직.toString() -> {
                    form.getField("ORG").setValue(score)
                }

                QuestionGroup.CAREER_업계.toString() -> {
                    form.getField("FIELD").setValue(score)
                }

                QuestionGroup.CAREER_전문분야.toString() -> {
                    form.getField("EXPERTISE").setValue(score)
                }

                QuestionGroup.CAREER_영역확장.toString() -> {
                    form.getField("EXPAND").setValue(score)
                }

                QuestionGroup.CAREER_영역개발.toString() -> {
                    form.getField("DEV").setValue(score)
                }

                QuestionGroup.CAREER_업무재조성.toString() -> {
                    form.getField("REORG").setValue(score)
                }

                QuestionGroup.CAREER_지원개발.toString() -> {
                    form.getField("APPLY").setValue(score)
                }

                QuestionGroup.CAREER_학습계획.toString() -> {
                    form.getField("STUDYPLAN").setValue(score)
                }

                QuestionGroup.CAREER_실천.toString() -> {
                    form.getField("DO").setValue(score)
                }

                QuestionGroup.CAREER_월드지수.toString() -> {
                    form.getField("WORLD").setValue(score)
                }

                QuestionCtype.CHARACTER.toString() -> {
                    form.getField("CHARACTER").setValue(score)
                }

                QuestionCtype.CONNECTION.toString() -> {
                    form.getField("CONNECTION").setValue(score)
                }

                QuestionCtype.CONDITION.toString() -> {
                    form.getField("CONDITION").setValue(score)
                }

                QuestionCtype.CHALLENGE.toString() -> {
                    form.getField("CHALLENGE").setValue(score)
                }

                QuestionCtype.CONTROL.toString() -> {
                    form.getField("CONTROL").setValue(score)
                }

                "age" -> {
                    form.getField("LOC").setValue(score)
                }

                QuestionANM.ABILITY.toString() -> {
                    form.getField("AB").setValue(score)
                }

                QuestionANM.NETWORK_POWER.toString() -> {
                    form.getField("NETWORKPOWER").setValue(score)
                }

                QuestionANM.MOVE_ON.toString() -> {
                    form.getField("MO").setValue(score)
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
