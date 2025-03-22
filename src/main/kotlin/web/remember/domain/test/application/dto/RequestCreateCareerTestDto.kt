package web.remember.domain.test.application.dto

data class RequestCreateCareerTestDto(
    val questionMap: Map<String, Map<String, String>>,
    val memberId: String,
)
