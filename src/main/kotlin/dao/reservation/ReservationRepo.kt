package dao.reservation

import model.Reservation
import java.util.LinkedList

class ReservationRepo {
    val reservationSet: MutableSet<Reservation> = mutableSetOf()
    val waitList: LinkedList<Reservation> = LinkedList()
}
