package service.reservation

import model.Reservation
import java.time.LocalDate
import java.util.UUID

interface ReservationService {
    fun createReservation(reservation: Reservation, addToWaitList: Boolean = true): UUID
    fun updateReservation(reservationId: UUID, updatedReservation: Reservation): UUID
    fun deleteReservation(reservationId: UUID): UUID?
    fun getReservation(reservationId: UUID): Reservation
    fun getReservationsOfTheDay(date: LocalDate): List<Reservation>
    fun getSummary(dateOfReservation: LocalDate): String
    fun addToWaitList(reservation: Reservation): UUID
    fun removeFromWaitListAndCheckIn(reservation: Reservation): Reservation
    fun checkInReservation(reservationId: UUID): Reservation
    fun checkoutReservation(reservationId: UUID): Reservation
}
