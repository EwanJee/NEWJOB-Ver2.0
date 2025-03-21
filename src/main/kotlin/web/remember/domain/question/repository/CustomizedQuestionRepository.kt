package web.remember.domain.question.repository

import web.remember.domain.question.repository.dto.ResponseFinishTestDto

interface CustomizedQuestionRepository {
    fun findAnmsAndScoreAndCtypeById(id: String): ResponseFinishTestDto
}
