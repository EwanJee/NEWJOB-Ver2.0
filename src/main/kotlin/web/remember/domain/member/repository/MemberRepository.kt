package web.remember.domain.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import web.remember.domain.member.entity.Member
import web.remember.domain.member.repository.jdsl.CustomizedMemberRepository

@Repository
interface MemberRepository :
    JpaRepository<Member, String>,
    CustomizedMemberRepository {
    @Query("SELECT m.id FROM Member m WHERE m.phoneNumber = :phoneNumber")
    fun findIdByPhoneNumber(phoneNumber: String): String?

    fun existsByPhoneNumberAndName(
        phoneNumber: String,
        name: String,
    ): Boolean

    fun findByEmailL(email: String): Member?

    fun findByKakaoId(kakaoId: Long): Member?
}
