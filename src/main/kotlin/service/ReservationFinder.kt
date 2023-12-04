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
        val areTablesAvailable = reservationRepo.reservationSet.any {
            (reservationRepo.totalNumberOfTables * reservationRepo.numberOfSeatingsPerTable) > reservationToAdd.totalNumberOfPeople
        }
        if (!areTablesAvailable) {
            return false
        }

        val doesReservationExist = reservationRepo.reservationSet.any {
            isWithin90Minutes(it.timeOfTheReservation, reservationToAdd.timeOfTheReservation)
        }
        if (doesReservationExist) {
            return false
        }

        return true
    }

    private fun isWithin90Minutes(time1: LocalTime, time2: LocalTime): Boolean {
        val diff = abs(time1.until(time2, ChronoUnit.MINUTES))
        return diff <= 90
    }
}