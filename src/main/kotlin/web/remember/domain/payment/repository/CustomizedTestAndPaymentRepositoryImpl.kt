package web.remember.domain.payment.repository

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.support.spring.data.jpa.extension.createQuery
import jakarta.persistence.EntityManager
import jakarta.persistence.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import web.remember.domain.payment.Payment
import web.remember.domain.test.entity.Test

@Repository
class CustomizedTestAndPaymentRepositoryImpl(
    private val entityManager: EntityManager,
    private val context: JpqlRenderContext,
) : CustomizedTestAndPaymentRepository {
    @Transactional
    override fun updateTestAndPaymentAfterPayment(
        testId: Long,
        paymentId: String,
    ): Boolean {
        val query1 =
            jpql {
                update(entity(Test::class))
                    .set(path(Test::paid), value(true))
                    .where(
                        path(Test::id).eq(testId),
                    )
            }
        val query2 =
            jpql {
                update(entity(Payment::class))
                    .set(path(Payment::status), "PAID")
                    .where(
                        path(Payment::id).eq(paymentId),
                    )
            }
        val jpaQuery1: Query = entityManager.createQuery(query1, context)
        val jpaQuery2: Query = entityManager.createQuery(query2, context)
        val result1 = jpaQuery1.executeUpdate()
        val result2 = jpaQuery2.executeUpdate()
        return result1 > 0 && result2 > 0
    }
}
