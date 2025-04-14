package dao.order

import model.MenuItem
import model.Order
import java.util.*

class OrderDaoImpl(
    private val orderRepo: OrderRepo,
) : OrderDao {
    override fun placeOrder(order: Order): UUID {
        orderRepo.orderList.add(order)
        return order.id
    }

    override fun addToOrder(id: UUID, menuItems: List<MenuItem>): UUID {
        TODO("Not yet implemented")
    }

    override fun cancelOrder(id: UUID): Order {
        val order = orderRepo.orderList.firstOrNull {
            it.id == id
        } ?: throw java.lang.Exception("Order not found")

        orderRepo.orderList.removeIf {
            it.id == id
        }
        return order
    }
}
