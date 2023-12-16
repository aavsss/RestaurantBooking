package dao

import model.Reservation
import java.time.LocalTime
import java.util.LinkedList

class ReservationRepo {
    val reservationSet: MutableSet<Reservation> = mutableSetOf()
    val waitList: LinkedList<Reservation> = LinkedList()

    // restaurant config
    val totalNumberOfTables = 5
    val numberOfSeatingPerTable = 2
    val startTime: LocalTime = LocalTime.of(4, 0, 0)
    val endTime: LocalTime = LocalTime.of(9, 0, 0)
}
