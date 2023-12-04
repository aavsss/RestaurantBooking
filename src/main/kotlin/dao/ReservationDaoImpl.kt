package dao

import model.Reservation
import java.time.LocalDate
import java.util.*

class ReservationDaoImpl : ReservationDao {

    private val reservationSet: MutableSet<Reservation> = mutableSetOf()

    override fun createReservation(reservation: Reservation): UUID {
        // todo algorithm lies here. find if any reservation is within 90 mins of it
        val doesReservationExist = reservationSet.any {
            it.timeOfTheReservation == reservation.timeOfTheReservation
        }
        if (doesReservationExist) {
            throw java.lang.Exception("Reservation already exists")
        }
        reservationSet.add(reservation)
        return reservation.id
    }

    override fun updateReservation(reservationId: UUID, updatedReservation: Reservation): UUID {
        val initialReservation = reservationSet.find {
            it.id == reservationId
        }
        if (initialReservation != null) {
            reservationSet.remove(initialReservation)

            reservationSet.add(updatedReservation)
        }
        return updatedReservation.id
    }

    override fun deleteReservation(reservationId: UUID): UUID {
        reservationSet.removeIf {
            it.id == reservationId
        }
        return reservationId
    }

    override fun getReservation(reservationId: UUID): Reservation {
        val reservation = reservationSet.find {
            it.id == reservationId
        } ?: throw java.lang.Exception("Element not found with id $reservationId")

        return reservation
    }

    override fun getReservationsOfTheDay(date: LocalDate): List<Reservation> {
        val reservations = reservationSet.filter {
            it.dayOfTheReservation == date
        }
        return reservations
    }
}