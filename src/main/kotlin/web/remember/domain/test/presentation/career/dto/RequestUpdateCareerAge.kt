package web.remember.domain.test.presentation.career.dto

import web.remember.domain.error.annotation.CheckNotEmptyString

data class RequestUpdateCareerAge(
    @field:CheckNotEmptyString(message = "경력 등고선을 입력해주세요.")
    val age: String,
)
