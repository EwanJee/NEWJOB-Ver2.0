package web.remember.domain.member.repository.jdsl

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.support.spring.data.jpa.extension.createQuery
import jakarta.persistence.EntityManager
import jakarta.persistence.Query
import org.springframework.stereotype.Repository
import web.remember.domain.member.entity.Member
import web.remember.domain.member.repository.jdsl.dto.ResponseNameAndIndustry

@Repository
class CustomizedMemberRepositoryImpl(
    private val entityManager: EntityManager,
    private val context: JpqlRenderContext,
) : CustomizedMemberRepository {
    override fun findNameAndIndustryById(id: String): ResponseNameAndIndustry {
        val query =
            jpql {
                selectNew<ResponseNameAndIndustry>(
                    path(Member::name),
                    path(Member::industry),
                ).from(
                    entity(Member::class),
                ).where(
                    path(Member::id).eq(id),
                )
            }
        val jpaQuery: Query = entityManager.createQuery(query, context)
        val result = jpaQuery.resultList
        return result[0] as ResponseNameAndIndustry
    }
}
