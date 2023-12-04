package dao

import model.Reservation

class ReservationRepo {
    val reservationSet: MutableSet<Reservation> = mutableSetOf()
    val totalNumberOfTables = 5
    val numberOfSeatingsPerTable = 2
}