@file:Suppress("ktlint:standard:no-wildcard-imports")

package web.remember.domain.payment.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import web.remember.domain.payment.Item

@Repository
interface ItemRepository : JpaRepository<Item, String>
