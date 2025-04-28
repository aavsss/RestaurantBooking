package service.reservation

import model.Reservation
import java.util.*

interface WaitListService {
    fun addToWaitList(reservationId: UUID): UUID
    fun getWaitList(): MutableList<Reservation>
}
