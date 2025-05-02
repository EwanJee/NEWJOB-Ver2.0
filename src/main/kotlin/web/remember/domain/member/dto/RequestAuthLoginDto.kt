package web.remember.domain.member.dto

data class RequestAuthLoginDto(
    val phoneNumber: String,
    val name: String,
    val industry: String,
    val email: String,
)
