package service

import dao.ReservationDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import model.Reservation
import java.time.LocalDate
import java.util.*

class ReservationServiceImpl(
    private val reservationDao: ReservationDao,
    private val reservationFinder: ReservationFinder,
) : ReservationService {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun createReservation(reservation: Reservation): UUID {
        if (!reservationFinder.isReservationValidToUpsert(reservation)) {
            val alternateTimes = reservationFinder.findAlternateDates(reservation.dayOfTheReservation)
            throw Exception("Time already booked. Pick some other time: $alternateTimes")
        }
        return reservationDao.createReservation(reservation)
    }

    override fun updateReservation(reservationId: UUID, updatedReservation: Reservation): UUID {
        return reservationDao.updateReservation(reservationId, updatedReservation)
    }

    override fun deleteReservation(reservationId: UUID): UUID? = runBlocking {
        return@runBlocking try {
            val deletedUuid = reservationDao.deleteReservation(reservationId)
            ReservationEventBusImpl.publish(reservationId)
            deletedUuid
        } catch (e: Exception) {
            null
        }
    }

    override fun getReservation(reservationId: UUID): Reservation {
        return reservationDao.getReservation(reservationId)
    }

    override fun getReservationsOfTheDay(date: LocalDate): List<Reservation> {
        return reservationDao.getReservationsOfTheDay(date)
    }

    override fun getSummary(dateOfReservation: LocalDate): String {
        return reservationDao.getSummary(dateOfReservation)
    }

    override fun addToWaitList(reservation: Reservation): UUID {
        return reservationDao.addToWaitList(reservation)
    }
}
