package service.order

import model.MenuItem
import model.Order
import java.util.*

interface OrderService {
    fun placeOrder(order: Order): UUID
    fun addToOrder(id: UUID, menuItems: List<MenuItem>): UUID
    fun cancelOrder(id: UUID): Order
}
