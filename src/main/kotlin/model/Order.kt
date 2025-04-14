package model

import java.util.*

data class Order(
    val id: UUID,
    val reservationId: UUID,
    val menuItems: MutableList<MenuItem>,
    val tableId: Int,
    private val total: Long = 0L,
) {
    fun calculateTotal(): Long {
        return menuItems.sumOf { it.price }
    }
}
