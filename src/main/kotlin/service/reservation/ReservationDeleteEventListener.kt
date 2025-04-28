package service.reservation

import dao.reservation.ReservationRepo
import model.ReservationStatus

class ReservationDeleteEventListener(
    private val reservationDeleteEventHandlerImpl: ReservationDeleteEventHandlerImpl,
    private val reservationService: ReservationService,
    private val waitListService: WaitListService,
) {

    init {
        startListening()
    }

    private fun startListening() {
        reservationDeleteEventHandlerImpl.subscribe {
            val reservationToBeAdded = waitListService.getWaitList().first {
                it.status == ReservationStatus.WAITING_TO_BE_CHECKED_IN
            }
            reservationService.removeFromWaitListAndCheckIn(reservationToBeAdded)
        }
    }
}
