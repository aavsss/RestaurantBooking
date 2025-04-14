package service.reservation

import dao.RestaurantConfig
import dao.reservation.ReservationRepo
import model.Reservation
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.math.abs

class ReservationFinder(
    private val reservationRepo: ReservationRepo,
    private val restaurantConfig: RestaurantConfig,
) {
    fun isReservationValidToUpsert(
        reservationToAdd: Reservation,
        reservationIdsToExclude: List<UUID> = emptyList(),
    ): Boolean {
        val reservationsWithin90Mins = reservationRepo.reservationSet
            .filter { !reservationIdsToExclude.contains(it.id) }
            .filter {
                isWithin90Minutes(it.timeOfTheReservation, reservationToAdd.timeOfTheReservation)
            }
        val numberOfTablesNeeded = (addOneToSeatIfOddNumberOfPeople(reservationToAdd.totalNumberOfPeople) / restaurantConfig.numberOfSeatingPerTable)

        if (reservationsWithin90Mins.isNotEmpty()) {
            val numberOfTablesTaken = reservationsWithin90Mins.sumOf {
                (addOneToSeatIfOddNumberOfPeople(it.totalNumberOfPeople)) / restaurantConfig.numberOfSeatingPerTable
            }

            if (
                (numberOfTablesTaken + numberOfTablesNeeded) > restaurantConfig.totalNumberOfTables
            ) {
                return false
            }
        }

        if (numberOfTablesNeeded > restaurantConfig.totalNumberOfTables) return false
        return true
    }

    fun findAlternateDates(dayOfTheReservation: LocalDate): List<LocalTime> {
        var currentTime = restaurantConfig.startTime
        val times = mutableListOf<LocalTime>()
        while (currentTime.isBefore(restaurantConfig.endTime)) {
            val free90MinWindow = reservationRepo.reservationSet
                .filter { it.dayOfTheReservation == dayOfTheReservation }
                .any { isWithin90Minutes(it.timeOfTheReservation, currentTime) }
            if (!free90MinWindow) {
                times.add(currentTime)
            }
            currentTime = currentTime.plusMinutes(30)
        }
        return times
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
