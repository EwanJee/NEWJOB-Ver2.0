package web.remember.domain.test.application.pdf

import com.itextpdf.forms.PdfAcroForm
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.PdfWriter
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import web.remember.domain.error.CustomException
import web.remember.domain.question.entity.QuestionANM
import web.remember.domain.question.entity.QuestionCtype
import web.remember.domain.question.entity.QuestionGroup
import java.io.ByteArrayInputStream

@Service
class PdfServiceImpl : PdfService {
    override fun makeCareerPdf(
        name: String,
        data: MutableMap<String, String>,
        pdf: MultipartFile,
    ): ByteArray {
        var inputStream: ByteArrayInputStream? = null
        var reader: PdfReader? = null
        var outputStream: ByteArrayOutputStream? = null
        var writer: PdfWriter? = null
        var pdfDoc: PdfDocument? = null

        try {
            inputStream = ByteArrayInputStream(pdf.bytes)
            reader = PdfReader(inputStream)
            outputStream = ByteArrayOutputStream()
            writer = PdfWriter(outputStream)
            pdfDoc = PdfDocument(reader, writer)
            val form = PdfAcroForm.getAcroForm(pdfDoc, true)

            data.entries.forEach {
                val score = it.value
                when (it.key) {
                    QuestionGroup.CAREER_WHY.toString() -> {
                        if (form.getField("WHY") != null) form.getField("WHY").setValue(score)
                    }

                    QuestionGroup.CAREER_HOW.toString() -> {
                        if (form.getField("HOW") != null) form.getField("HOW").setValue(score)
                    }

                    QuestionGroup.CAREER_WHAT.toString() -> {
                        if (form.getField("WHAT") != null) form.getField("WHAT").setValue(score)
                    }

                    QuestionGroup.CAREER_성과평가.toString() -> {
                        if (form.getField("SUCCESS") != null) form.getField("SUCCESS").setValue(score)
                    }

                    QuestionGroup.CAREER_네트워크.toString() -> {
                        if (form.getField("NETWORK") != null) form.getField("NETWORK").setValue(score)
                    }

                    QuestionGroup.CAREER_팀웍동료.toString() -> {
                        if (form.getField("TEAMWORK") != null) form.getField("TEAMWORK").setValue(score)
                    }

                    QuestionGroup.CAREER_조직.toString() -> {
                        if (form.getField("ORG") != null) form.getField("ORG").setValue(score)
                    }

                    QuestionGroup.CAREER_업계.toString() -> {
                        if (form.getField("FIELD") != null) form.getField("FIELD").setValue(score)
                    }

                    QuestionGroup.CAREER_전문분야.toString() -> {
                        if (form.getField("EXPERTISE") != null) form.getField("EXPERTISE").setValue(score)
                    }

                    QuestionGroup.CAREER_영역확장.toString() -> {
                        if (form.getField("EXPAND") != null) form.getField("EXPAND").setValue(score)
                    }

                    QuestionGroup.CAREER_영역개발.toString() -> {
                        if (form.getField("DEV") != null) form.getField("DEV").setValue(score)
                    }

                    QuestionGroup.CAREER_업무재조성.toString() -> {
                        if (form.getField("REORG") != null) form.getField("REORG").setValue(score)
                    }

                    QuestionGroup.CAREER_지원개발.toString() -> {
                        if (form.getField("APPLY") != null) form.getField("APPLY").setValue(score)
                    }

                    QuestionGroup.CAREER_학습계획.toString() -> {
                        if (form.getField("STUDYPLAN") != null) form.getField("STUDYPLAN").setValue(score)
                    }

                    QuestionGroup.CAREER_실천.toString() -> {
                        if (form.getField("DO") != null) form.getField("DO").setValue(score)
                    }

                    QuestionGroup.CAREER_월드지수.toString() -> {
                        if (form.getField("WORLD") != null) form.getField("WORLD").setValue(score)
                    }

                    QuestionCtype.CHARACTER.toString() -> {
                        if (form.getField("CHARACTER") != null) form.getField("CHARACTER").setValue(score)
                    }

                    QuestionCtype.CONNECTION.toString() -> {
                        if (form.getField("CONNECTION") != null) form.getField("CONNECTION").setValue(score)
                    }

                    QuestionCtype.CONDITION.toString() -> {
                        if (form.getField("CONDITION") != null) form.getField("CONDITION").setValue(score)
                    }

                    QuestionCtype.CHALLENGE.toString() -> {
                        if (form.getField("CHALLENGE") != null) form.getField("CHALLENGE").setValue(score)
                    }

                    QuestionCtype.CONTROL.toString() -> {
                        if (form.getField("CONTROL") != null) form.getField("CONTROL").setValue(score)
                    }

                    "age" -> {
                        if (form.getField("LOC") != null) form.getField("LOC").setValue("나의 경력 등고선 위치: $score")
                    }

                    QuestionANM.ABILITY.toString() -> {
                        if (form.getField("AB") != null) form.getField("AB").setValue("나의 업계 능력 점수: $score")
                    }

                    QuestionANM.NETWORK_POWER.toString() -> {
                        if (form.getField("NETWORKPOWER") != null) form.getField("NETWORKPOWER").setValue("나의 네트워크 파워 점수: $score")
                    }

                    QuestionANM.MOVE_ON.toString() -> {
                        if (form.getField("MO") != null) form.getField("MO").setValue("나의 전직 가능성 점수: $score")
                    }
                }
            }

            // 모든 변경사항이 적용되었는지 확인
            form.flattenFields()

            // 문서 닫기
            pdfDoc.close()

            return outputStream.toByteArray()
        } catch (e: Exception) {
            throw CustomException("PDF 변환 중 오류가 발생했습니다.")
        } finally {
            // 모든 리소스 명시적으로 닫기
            try {
                pdfDoc?.close()
                writer?.close()
                outputStream?.close()
                reader?.close()
                inputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun test(pdf: MultipartFile): ByteArray {
        var inputStream: ByteArrayInputStream? = null
        var reader: PdfReader? = null
        var outputStream: ByteArrayOutputStream? = null
        var writer: PdfWriter? = null
        var pdfDoc: PdfDocument? = null

        try {
            inputStream = ByteArrayInputStream(pdf.bytes)
            reader = PdfReader(inputStream)
            outputStream = ByteArrayOutputStream()
            writer = PdfWriter(outputStream)
            pdfDoc = PdfDocument(reader, writer)

            val form = PdfAcroForm.getAcroForm(pdfDoc, true)

            // 폼 필드가 존재하는지 확인
            if (form.getField("WHY") == null) {
                throw CustomException("PDF 파일이 올바르지 않습니다.")
            }
            if (form.getField("WHY") != null) form.getField("WHY").setValue("score1")
            if (form.getField("HOW") != null) form.getField("HOW").setValue("score2")
            if (form.getField("WHAT") != null) form.getField("WHAT").setValue("score3")
            if (form.getField("SUCCESS") != null) form.getField("SUCCESS").setValue("score4")

            // 변경사항을 플랫닝 (옵션)
            form.flattenFields()

            // 문서 닫기 전에 변경사항 커밋
            pdfDoc.close()

            return outputStream.toByteArray()
        } catch (e: Exception) {
            // 에러 로깅
            throw CustomException("PDF 변환 중 오류가 발생했습니다.")
        } finally {
            // 모든 리소스 명시적으로 닫기
            try {
                pdfDoc?.close()
                writer?.close()
                outputStream?.close()
                reader?.close()
                inputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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
