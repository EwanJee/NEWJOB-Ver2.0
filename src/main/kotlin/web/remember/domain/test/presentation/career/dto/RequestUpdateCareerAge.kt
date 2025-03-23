package web.remember.domain.test.presentation.career.dto

import web.remember.domain.error.annotation.CheckNotEmptyString

data class RequestUpdateCareerAge(
    @field:CheckNotEmptyString(message = "테스트 ID를 입력해주세요.")
    val testId: String,
    @field:CheckNotEmptyString(message = "멤버 ID를 입력해주세요.")
    val memberId: String,
    @field:CheckNotEmptyString(message = "경력 등고선을 입력해주세요.")
    val age: String,
)
