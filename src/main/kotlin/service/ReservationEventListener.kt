package service

import dao.ReservationRepo
import kotlinx.coroutines.*
import java.util.UUID

class ReservationEventListener(
    private val reservationRepo: ReservationRepo,
) {

    fun startListening() {
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            ReservationEventBusImpl.subscribe<UUID> {
                // get another reservation from wait-list
                reservationRepo.reservationSet.add(
                    reservationRepo.waitList.pop(),
                )
            }
        }
    }
}
