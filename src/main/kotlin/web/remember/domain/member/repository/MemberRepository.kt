package web.remember.domain.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import web.remember.domain.member.entity.Member
import web.remember.domain.member.repository.jdsl.CustomizedMemberRepository

@Repository
interface MemberRepository :
    JpaRepository<Member, String>,
    CustomizedMemberRepository {
    fun existsByPhoneNumber(phoneNumber: String): Boolean
}
