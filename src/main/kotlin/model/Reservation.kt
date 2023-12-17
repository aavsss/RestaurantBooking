package model

import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class Reservation(
    val id: UUID,
    val name: String,
    val totalNumberOfPeople: Int,
    val totalTime: Long = 90L,
    val dayOfTheReservation: LocalDate,
    val timeOfTheReservation: LocalTime,
    var status: ReservationStatus = ReservationStatus.OPEN,
)
