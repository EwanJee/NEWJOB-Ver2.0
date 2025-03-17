package web.remember.domain.member.dto

import jakarta.validation.constraints.Min
import org.intellij.lang.annotations.RegExp

data class RequestCreateMemberDto(
    @Min(1)
    val name: String,
    @RegExp(prefix = "^(010-?)?\\d{3,4}-?\\d{4}\$")
    val phoneNumber: String,
)
