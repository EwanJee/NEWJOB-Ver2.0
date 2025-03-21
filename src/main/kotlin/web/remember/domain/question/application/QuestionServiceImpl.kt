package web.remember.domain.question.application

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import web.remember.domain.error.CustomException
import web.remember.domain.question.entity.Question
import web.remember.domain.question.entity.QuestionANM
import web.remember.domain.question.entity.QuestionGroup
import web.remember.domain.question.repository.QuestionRepository
import java.io.BufferedReader
import java.io.InputStreamReader

@Service
class QuestionServiceImpl(
    private val questionRepository: QuestionRepository,
) : QuestionService {
    @Transactional
    override fun create(file: MultipartFile): Long {
        val list = mutableListOf<Question>()

        var length: Long = 0L
        try {
            if (file.isEmpty) {
                throw CustomException("파일이 비어있습니다. 다시 시도해주세요")
            }
            file.originalFilename ?: throw CustomException("파일 이름이 없습니다. 다시 시도해주세요")
            if (!file.originalFilename!!.endsWith(".csv")) {
                throw CustomException("csv 파일만 업로드 가능합니다")
            }

            val reader = BufferedReader(InputStreamReader(file.inputStream))
            val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim())

            for (csvRecord in csvParser) {
                val group = csvRecord.get("그룹").trim()
                var score = csvRecord.get("점수").trim()
                val anms: List<String> = csvRecord.get("ANM").trim().split(",")
                val content = csvRecord.get("질문").trim()

                println("group: $group, score: $score, anms: $anms, content: $content")

                val convertedAnms: MutableList<QuestionANM>? =
                    anms
                        .mapNotNull { anm ->
                            val trimmed = anm.trim()
                            if (trimmed.isBlank()) return@mapNotNull null
                            try {
                                QuestionANM.valueOf(trimmed)
                            } catch (e: Exception) {
                                throw CustomException("ANM이 잘못되었습니다: $anm")
                            }
                        }.takeIf { it.isNotEmpty() }
                        ?.toMutableList()

                if (!QuestionGroup.entries.map { it.name }.contains(group)) {
                    throw CustomException("그룹이 잘못되었습니다: $group")
                }

                if (score.toIntOrNull() == null) {
                    score = "1"
                }

                val question =
                    Question(
                        group = QuestionGroup.valueOf(group),
                        score = score.toInt(),
                        anm = convertedAnms,
                        content = content,
                    )
                list.add(question)
                length++
            }
        } catch (e: Exception) {
            throw CustomException("CSV 파일을 처리하는 중 오류가 발생했습니다: ${e.message}")
        }

        questionRepository.saveAll(list)
        return length
    }
}
