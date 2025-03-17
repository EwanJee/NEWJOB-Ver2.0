package web.remember.domain.question.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import web.remember.domain.question.entity.Question

@Repository
interface QuestionRepository : JpaRepository<Question, Long>
