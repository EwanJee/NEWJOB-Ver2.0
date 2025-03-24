package web.remember.domain.question.application.dto

import web.remember.domain.question.entity.QuestionANM
import web.remember.domain.question.entity.QuestionCtype

class ResponseFinishTestDto(
    anms: MutableList<QuestionANM>?,
    val score: Int,
    ctype: QuestionCtype,
) {
    val anms: List<String> = anms?.map { it.name } ?: emptyList()
    val ctype: String = ctype.name
}
