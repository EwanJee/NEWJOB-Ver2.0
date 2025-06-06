package web.remember.domain.test.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import web.remember.domain.test.entity.Test

@Repository
interface TestRepository : JpaRepository<Test, Long> {
    @Query("SELECT t.data FROM Test t WHERE t.id = :testId")
    fun findDataById(testId: Long): MutableMap<String, String>?

    @Query("SELECT t.paid FROM Test t WHERE t.id = :testId AND t.memberId = :memberId")
    fun findPaidByIdAndMemberId(
        testId: Long,
        memberId: String,
    ): Boolean?

    fun findByMemberId(
        memberId: String,
        pageable: Pageable,
    ): Page<Test>
}
