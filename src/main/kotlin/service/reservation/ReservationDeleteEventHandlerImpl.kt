package service.reservation

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.UUID

class ReservationDeleteEventHandlerImpl {
    private val mutableEvents = MutableSharedFlow<UUID>()
    val events = mutableEvents.asSharedFlow()

    suspend fun publish(event: UUID) {
        mutableEvents.emit(event)
    }

    inline fun subscribe(crossinline onEvent: (UUID) -> Unit) = runBlocking {
        val subscribingScope = CoroutineScope(SupervisorJob())
        subscribingScope.launch {
            events.collect {
                onEvent(it)
            }
        }
    }
}
