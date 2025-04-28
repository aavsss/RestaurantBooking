package service.reservation

import dao.reservation.ReservationDao
import model.Reservation
import model.ReservationStatus
import java.util.*

class WaitListServiceImpl(
    private val reservationDao: ReservationDao,
) : WaitListService {

    override fun addToWaitList(reservationId: UUID): UUID {
        val reservation = reservationDao.getReservation(reservationId)
        reservation.status = ReservationStatus.WAITING_TO_BE_CHECKED_IN
        return reservationDao.addToWaitList(reservation)
    }

    override fun getWaitList(): MutableList<Reservation> {
        return reservationDao.getWaitList()
    }
}
