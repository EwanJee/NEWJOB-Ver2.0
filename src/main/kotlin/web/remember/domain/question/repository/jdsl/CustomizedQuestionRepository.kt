package web.remember.domain.question.repository.jdsl

import web.remember.domain.question.application.dto.ResponseFinishTestDto

interface CustomizedQuestionRepository {
    fun findAnmsAndScoreAndCtypeById(id: String): ResponseFinishTestDto
}
