package service

import model.Reservation
import java.time.LocalDate
import java.util.UUID

interface ReservationService {
    fun createReservation(reservation: Reservation): UUID
    fun updateReservation(reservationId: UUID, updatedReservation: Reservation): UUID
    fun deleteReservation(reservationId: UUID): UUID?
    fun getReservation(reservationId: UUID): Reservation
    fun getReservationsOfTheDay(date: LocalDate): List<Reservation>
    fun getSummary(dateOfReservation: LocalDate): String
    fun addToWaitList(reservation: Reservation): UUID
}
