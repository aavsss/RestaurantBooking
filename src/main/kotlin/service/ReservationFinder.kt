package service

import dao.ReservationRepo
import model.Reservation
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.abs

class ReservationFinder (
    private val reservationRepo: ReservationRepo
) {
    fun isReservationValid(reservationToAdd: Reservation): Boolean {
        val reservationsWithin90Mins = reservationRepo.reservationSet.filter {
            isWithin90Minutes(it.timeOfTheReservation, reservationToAdd.timeOfTheReservation)
        }
        val numberOfTablesNeeded = (addOneToSeatIfOddNumberOfPeople(reservationToAdd.totalNumberOfPeople) / reservationRepo.numberOfSeatingsPerTable)

        if (reservationsWithin90Mins.isNotEmpty()) {
            val numberOfTablesTaken = reservationsWithin90Mins.sumOf {
                (addOneToSeatIfOddNumberOfPeople(it.totalNumberOfPeople)) / reservationRepo.numberOfSeatingsPerTable
            }

            if (
                (numberOfTablesTaken + numberOfTablesNeeded) > reservationRepo.totalNumberOfTables
            ) return false
        }

        if (numberOfTablesNeeded > reservationRepo.totalNumberOfTables) return false
        return true
    }

    private fun addOneToSeatIfOddNumberOfPeople(numberOfPeople: Int): Int {
        if (numberOfPeople % 2 == 1) return numberOfPeople + 1
        return numberOfPeople
    }

    private fun isWithin90Minutes(time1: LocalTime, time2: LocalTime): Boolean {
        val diff = abs(time1.until(time2, ChronoUnit.MINUTES))
        return diff <= 90
    }
}