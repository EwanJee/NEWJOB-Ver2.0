package web.remember.domain.test.presentation.career.dto

import web.remember.domain.error.annotation.CheckNotEmptyString

data class RequestScoreUpdateDto(
    @field:CheckNotEmptyString(message = "회원 ID를 입력해주세요.")
    val memberId: String,
    @field:CheckNotEmptyString(message = "테스트 ID를 입력해주세요.")
    val testId: String,
    val scoreMap: Map<String, Int>,
)
