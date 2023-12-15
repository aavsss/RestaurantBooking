package service

import dao.ReservationRepo
import kotlinx.coroutines.*
import java.util.UUID

class ReservationEventListener(
    reservationRepo: ReservationRepo,
) {
    init {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            ReservationEventBusImpl.subscribe<UUID> { uuid ->
                // get another reservation from wait-list
                reservationRepo.reservationSet.add(
                    reservationRepo.waitList.pop(),
                )
            }
        }
    }
}
