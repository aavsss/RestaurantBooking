package dao.order

import model.MenuItem
import model.Order
import model.Table
import java.util.*

interface OrderDao {
    fun placeOrder(order: Order): UUID
    fun addToOrder(id: UUID, menuItems: List<MenuItem>): UUID
    fun cancelOrder(id: UUID): Order
}
