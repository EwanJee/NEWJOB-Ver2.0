package web.remember.domain.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import web.remember.domain.member.entity.Member

@Repository
interface MemberRepository : JpaRepository<Member, String>
