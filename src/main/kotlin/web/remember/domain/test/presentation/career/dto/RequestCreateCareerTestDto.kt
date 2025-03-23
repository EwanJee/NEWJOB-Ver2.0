package web.remember.domain.test.presentation.career.dto

import web.remember.domain.error.annotation.CheckNotEmptyString

data class RequestCreateCareerTestDto(
    val questionMap: Map<String, Map<String, String>>,
    @field:CheckNotEmptyString(message = "회원 ID를 입력해주세요.")
    val memberId: String,
)
