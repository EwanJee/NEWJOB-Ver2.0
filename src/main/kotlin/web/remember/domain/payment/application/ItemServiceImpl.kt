package web.remember.domain.payment.application

import org.springframework.stereotype.Service
import web.remember.domain.error.CustomException
import web.remember.domain.payment.Item
import web.remember.domain.payment.repository.ItemRepository

@Service
class ItemServiceImpl(
    private val itemRepository: ItemRepository,
) : ItemService {
    override fun getItemById(itemId: String): Item =
        itemRepository.findById(itemId).orElseThrow {
            CustomException("해당 아이템이 존재하지 않습니다.")
        }
}
