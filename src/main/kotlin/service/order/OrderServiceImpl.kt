package service.order

import dao.order.OrderDao
import model.MenuItem
import model.Order
import java.util.*

class OrderServiceImpl(
    private val orderDao: OrderDao,
) : OrderService {

    override fun placeOrder(order: Order): UUID {
        return orderDao.placeOrder(order)
    }

    override fun addToOrder(id: UUID, menuItems: List<MenuItem>): UUID {
        return orderDao.addToOrder(id, menuItems)
    }

    override fun cancelOrder(id: UUID): Order {
        return orderDao.cancelOrder(id)
    }
}
