package web.remember.domain.question.application

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
interface QuestionService {
    fun create(file: MultipartFile): Long
}
