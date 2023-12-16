package dao

import model.Reservation
import service.ReservationFinder
import java.time.LocalDate
import java.util.*

class ReservationDaoImpl(
    private val reservationFinder: ReservationFinder,
    private val reservationRepo: ReservationRepo,
) : ReservationDao {

    override fun createReservation(reservation: Reservation): UUID {
        reservationRepo.reservationSet.add(reservation)
        return reservation.id
    }

    override fun updateReservation(reservationId: UUID, updatedReservation: Reservation): UUID {
        val initialReservation = reservationFinder.isReservationValidToUpsert(
            updatedReservation,
            listOf(reservationId),
        )
        if (initialReservation) {
            reservationRepo.reservationSet.removeIf {
                it.id == reservationId
            }

            reservationRepo.reservationSet.add(updatedReservation)
        }
        return updatedReservation.id
    }

    override fun deleteReservation(reservationId: UUID): UUID {
        val isRemoved = reservationRepo.reservationSet.removeIf {
            it.id == reservationId
        }

        if (!isRemoved) throw Exception("Reservation not found")
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

    override fun getSummary(dateOfReservation: LocalDate): String {
        val numberOfPeople = reservationRepo.reservationSet.sumOf {
            it.totalNumberOfPeople
        }
        return "Summary of the day $dateOfReservation: $numberOfPeople"
    }

    override fun getWaitList(): LinkedList<Reservation> {
        return reservationRepo.waitList
    }

    override fun addToWaitList(reservation: Reservation): UUID {
        reservationRepo.waitList.add(reservation)
        return reservation.id
    }
}
