package service.reservation

import dao.reservation.ReservationRepo

class ReservationDeleteEventListener(
    private val reservationRepo: ReservationRepo,
    private val reservationDeleteEventHandlerImpl: ReservationDeleteEventHandlerImpl,
) {

    init {
        startListening()
    }

    private fun startListening() {
        reservationDeleteEventHandlerImpl.subscribe {
            val waitListedReservation = reservationRepo.waitList.pop()
            reservationRepo.reservationSet.add(waitListedReservation)
        }
    }
}
