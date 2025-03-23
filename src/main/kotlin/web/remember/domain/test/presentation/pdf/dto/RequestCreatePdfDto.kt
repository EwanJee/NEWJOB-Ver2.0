package web.remember.domain.test.presentation.pdf.dto

import web.remember.domain.error.annotation.CheckNotEmptyString

data class RequestCreatePdfDto(
    @field:CheckNotEmptyString(message = "이름을 입력해주세요.")
    val name: String,
    @field:CheckNotEmptyString(message = "업계를 입력해주세요.")
    val industry: String,
    val data: MutableMap<String, String>,
)
