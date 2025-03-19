package web.remember.domain.question.application

import org.springframework.web.multipart.MultipartFile

interface QuestionService {
    fun create(file: MultipartFile): Long
}
