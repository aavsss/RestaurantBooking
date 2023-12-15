package service

import dao.ReservationDao
import model.Reservation
import java.time.LocalDate
import java.util.*

class ReservationServiceImpl(
    private val reservationDao: ReservationDao,
) : ReservationService {

    override fun createReservation(reservation: Reservation): UUID {
        return reservationDao.createReservation(reservation)
    }

    override fun updateReservation(reservationId: UUID, updatedReservation: Reservation): UUID {
        return reservationDao.updateReservation(reservationId, updatedReservation)
    }

    override fun deleteReservation(reservationId: UUID): UUID {
        return reservationDao.deleteReservation(reservationId)
    }

    override fun getReservation(reservationId: UUID): Reservation {
        return reservationDao.getReservation(reservationId)
    }

    override fun getReservationsOfTheDay(date: LocalDate): List<Reservation> {
        return reservationDao.getReservationsOfTheDay(date)
    }

    override fun getSummary(dateOfReservation: LocalDate): String {
        return reservationDao.getSummary(dateOfReservation)
    }
}
