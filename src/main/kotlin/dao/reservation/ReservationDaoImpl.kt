package dao.reservation

import model.Reservation
import model.ReservationStatus
import service.reservation.ReservationFinder
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
        val numberOfPeople = reservationRepo.reservationSet
            .filter { it.dayOfTheReservation == dateOfReservation }
            .sumOf { it.totalNumberOfPeople }
        return "Summary of the day $dateOfReservation: " +
            "Total number of people to serve today: $numberOfPeople" +
            "Number of people in waitList ${reservationRepo.waitList.size}"
    }

    override fun getWaitList(): LinkedList<Reservation> {
        return reservationRepo.waitList
    }

    override fun addToWaitList(reservation: Reservation): UUID {
        reservationRepo.waitList.add(reservation)
        return reservation.id
    }

    override fun checkInReservation(reservationId: UUID): Reservation {
        val reservation = getReservation(reservationId)
        reservation.status = ReservationStatus.CHECKED_IN
        return reservation
    }

    override fun checkoutReservation(reservationId: UUID): Reservation {
        val reservation = getReservation(reservationId)
        reservation.status = ReservationStatus.CHECKED_OUT
        return reservation
    }
}
