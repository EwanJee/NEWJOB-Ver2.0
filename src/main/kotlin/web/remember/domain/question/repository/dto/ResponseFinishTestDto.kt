package web.remember.domain.question.repository.dto

data class ResponseFinishTestDto(
    val anms: List<String>?,
    val score: Int,
    val ctype: String,
)
