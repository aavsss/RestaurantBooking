package dao

import model.Reservation
import service.ReservationFinder
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.abs

class ReservationDaoImpl (
    private val reservationFinder: ReservationFinder,
    private val reservationRepo: ReservationRepo
) : ReservationDao {

    override fun createReservation(reservation: Reservation): UUID {
        if (reservationFinder.isReservationValid(reservation)) {
            throw java.lang.Exception("Time already booked")
        }
        reservationRepo.reservationSet.add(reservation)
        return reservation.id
    }



    override fun updateReservation(reservationId: UUID, updatedReservation: Reservation): UUID {
        val initialReservation = reservationRepo.reservationSet.find {
            it.id == reservationId
        }
        if (initialReservation != null) {
            reservationRepo.reservationSet.remove(initialReservation)

            reservationRepo.reservationSet.add(updatedReservation)
        }
        return updatedReservation.id
    }

    override fun deleteReservation(reservationId: UUID): UUID {
        reservationRepo.reservationSet.removeIf {
            it.id == reservationId
        }
        return reservationId
    }

    override fun getReservation(reservationId: UUID): Reservation {
        val reservation = reservationRepo.reservationSet.find {
            it.id == reservationId
        } ?: throw java.lang.Exception("Element not found with id $reservationId")

        return reservation
    }

    override fun getReservationsOfTheDay(date: LocalDate): List<Reservation> {
        val reservations = reservationRepo.reservationSet.filter {
            it.dayOfTheReservation == date
        }
        return reservations
    }
}