package web.remember.domain.question.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import web.remember.domain.question.entity.Question
import web.remember.domain.question.entity.Type

@Repository
interface QuestionRepository : JpaRepository<Question, Long> {
    fun findByType(type: Type): List<Question>
}
