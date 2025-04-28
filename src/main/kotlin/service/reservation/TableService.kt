package service.reservation

import model.RestaurantBookingStatus
import model.Table
import java.util.*

interface TableService {
    fun assignTable(reservationId: UUID): Table
    fun unAssignTable(reservationId: UUID)
    fun getTableStatus(): MutableList<RestaurantBookingStatus>
}
