package web.remember.domain.member.repository.jdsl

import web.remember.domain.member.repository.jdsl.dto.ResponseNameAndIndustry

interface CustomizedMemberRepository {
    fun findNameAndIndustryById(id: String): ResponseNameAndIndustry
}
