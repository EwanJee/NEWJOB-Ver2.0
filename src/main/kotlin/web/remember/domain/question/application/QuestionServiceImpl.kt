package web.remember.domain.question.application

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.springframework.stereotype.Service
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
    override fun create(file: MultipartFile): Long {
        if (file.isEmpty) {
            throw CustomException("파일이 비어있습니다. 다시 시도해주세요")
        }
        file.originalFilename ?: throw CustomException("파일 이름이 없습니다. 다시 시도해주세요")
        if (!file.originalFilename!!.endsWith(".csv")) {
            throw CustomException("csv 파일만 업로드 가능합니다")
        }

        var length: Long? = null

        val list = mutableListOf<Question>()

        try {
            val reader = BufferedReader(InputStreamReader(file.inputStream))
            val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim())
            length = csvParser.records.size.toLong()

            if (length == 0L) {
                throw CustomException("파일에 데이터가 없습니다. 다시 시도해주세요")
            }

            for (csvRecord in csvParser) {
                val group = csvRecord.get("그룹")
                val score = csvRecord.get("점수")
                val anm = csvRecord.get("ANM")

                list.add(Question(QuestionGroup.valueOf(group), score.toInt(), QuestionANM.valueOf(anm)))
            }
        } catch (e: Exception) {
            throw CustomException("CSV 파일을 처리하는 중 오류가 발생했습니다: ${e.message}")
        }

        questionRepository.saveAll(list)
        return length
    }
}
