package web.remember.domain.payment.application

import web.remember.domain.payment.Item

interface ItemService {
    fun getItemById(itemId: String): Item
}
