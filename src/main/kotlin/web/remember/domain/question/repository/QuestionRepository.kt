package web.remember.domain.question.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import web.remember.domain.question.entity.Question
import web.remember.domain.question.entity.TestType

@Repository
interface QuestionRepository : JpaRepository<Question, Long> {
    fun findByType(testType: TestType): List<Question>

    @Query("SELECT q.group FROM Question q WHERE q.id = :id")
    fun findGroupById(id: String): String

    @Query("SELECT COALESCE(q.anm, 'NULL'), q.score, q.ctype FROM Question q WHERE q.id = :id")
    fun findAnmAndScoreAndCtypeById(id: String): List<String>
}
