package web.remember.domain.question.repository.jdsl

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderer
import com.linecorp.kotlinjdsl.support.spring.data.jpa.extension.createQuery
import jakarta.persistence.EntityManager
import jakarta.persistence.Query
import org.springframework.stereotype.Repository
import web.remember.domain.question.application.dto.ResponseFinishTestDto
import web.remember.domain.question.entity.Question

@Repository
class CustomizedQuestionRepositoryImpl(
//    private val executor: KotlinJdslJpqlExecutor,
    private val entityManager: EntityManager,
    private val context: JpqlRenderContext,
    private val renderer: JpqlRenderer,
) : CustomizedQuestionRepository {
    override fun findAnmsAndScoreAndCtypeById(id: String): ResponseFinishTestDto {
        val query =
            jpql {
                selectNew<ResponseFinishTestDto>(
                    path(Question::anms),
                    path(Question::score),
                    path(Question::ctype),
                ).from(
                    entity(Question::class),
                ).where(
                    path(Question::id).eq(id),
                )
            }
        val jpaQuery: Query = entityManager.createQuery(query, context)
        val result = jpaQuery.resultList
        return result[0] as ResponseFinishTestDto
    }
}
