package dao.reservation

import model.Reservation

class ReservationRepo {
    /**
     * Represents all the reservation
     */
    val reservationSet: MutableSet<Reservation> = mutableSetOf()
    val waitList: MutableList<Reservation> = mutableListOf()
    val pastReservationSet: MutableSet<Reservation> = mutableSetOf()
}
