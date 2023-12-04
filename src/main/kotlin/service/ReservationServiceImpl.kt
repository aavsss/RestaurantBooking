package service

import dao.ReservationDao
import model.Reservation
import java.time.LocalDate
import java.util.*

class ReservationServiceImpl (
    private val reservationDao: ReservationDao
) : ReservationService {

    override fun createReservation(reservation: Reservation): UUID {
        TODO("Not yet implemented")
    }

    override fun updateReservation(reservationId: UUID, updatedReservation: Reservation): UUID {
        TODO("Not yet implemented")
    }

    override fun deleteReservation(reservationId: UUID): UUID {
        TODO("Not yet implemented")
    }

    override fun getReservation(reservationId: UUID): Reservation {
        TODO("Not yet implemented")
    }

    override fun getReservationsOfTheDay(date: LocalDate): List<Reservation> {
        TODO("Not yet implemented")
    }
}
