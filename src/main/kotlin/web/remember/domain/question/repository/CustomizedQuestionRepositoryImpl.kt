package web.remember.domain.question.repository

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderer
import com.linecorp.kotlinjdsl.support.spring.data.jpa.extension.createQuery
import jakarta.annotation.PostConstruct
import jakarta.persistence.EntityManager
import jakarta.persistence.Query
import org.springframework.stereotype.Repository
import web.remember.domain.question.entity.Question
import web.remember.domain.question.repository.dto.ResponseFinishTestDto

@Repository
class CustomizedQuestionRepositoryImpl(
//    private val executor: KotlinJdslJpqlExecutor,
    private val entityManager: EntityManager,
) : CustomizedQuestionRepository {
    lateinit var context: JpqlRenderContext
    lateinit var renderer: JpqlRenderer

    @PostConstruct
    fun init() {
        context = JpqlRenderContext()
        renderer = JpqlRenderer()
    }

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
        println(query)
        val jpaQuery: Query = entityManager.createQuery(query, context)
        val result = jpaQuery.resultList
        val first: Any? = result?.get(0)
        println("anms = $first")
        return ResponseFinishTestDto(
            anms = result?.get(0) as List<String>,
            score = result[1] as Int,
            ctype = result[2] as String,
        )
    }
}
