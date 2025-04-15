package dao.reservation

import model.Reservation
import java.time.LocalDate
import java.util.*

interface ReservationDao {
    fun createReservation(reservation: Reservation): UUID
    fun updateReservation(reservationId: UUID, updatedReservation: Reservation): UUID
    fun deleteReservation(reservationId: UUID): UUID
    fun getReservation(reservationId: UUID): Reservation
    fun getReservationsOfTheDay(date: LocalDate): List<Reservation>
    fun getSummary(dateOfReservation: LocalDate): String
    fun getWaitList(): MutableList<Reservation>
    fun addToWaitList(reservation: Reservation): UUID
    fun removeFromWaitList(reservation: Reservation): Reservation
    fun checkInReservation(reservationId: UUID): Reservation
    fun checkoutReservation(reservationId: UUID): Reservation
}
