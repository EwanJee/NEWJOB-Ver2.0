package web.remember.domain.member.repository.jdsl.dto

import java.io.Serializable

data class ResponseNameAndIndustry(
    val name: String,
    val industry: String,
) : Serializable
