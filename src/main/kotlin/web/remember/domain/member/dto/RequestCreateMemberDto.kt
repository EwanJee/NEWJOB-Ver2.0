package web.remember.domain.member.dto

import web.remember.domain.error.annotation.CheckNotEmptyString
import web.remember.domain.error.annotation.CheckPhoneNumber

data class RequestCreateMemberDto(
    @field:CheckNotEmptyString(message = "이름을 입력해주세요.")
    val name: String,
    @field:CheckPhoneNumber(message = "올바른 전화번호를 입력해주세요. 예: 010-1234-5678")
    val phoneNumber: String,
    @field:CheckNotEmptyString(message = "업종을 입력해주세요.")
    val industry: String,
)
