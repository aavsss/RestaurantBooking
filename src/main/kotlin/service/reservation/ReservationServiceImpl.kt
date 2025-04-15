package service.reservation

import dao.RestaurantConfig
import dao.reservation.ReservationDao
import kotlinx.coroutines.runBlocking
import model.Reservation
import model.TableStatus
import java.time.LocalDate
import java.util.*

class ReservationServiceImpl(
    private val reservationDao: ReservationDao,
    private val reservationFinder: ReservationFinder,
    private val restaurantConfig: RestaurantConfig,
    private val reservationDeleteEventHandlerImpl: ReservationDeleteEventHandlerImpl,
) : ReservationService {

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
            reservationDeleteEventHandlerImpl.publish(reservationId)
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

    override fun removeFromWaitList(reservation: Reservation): Reservation {
        val firstInLineReservation = reservationDao.removeFromWaitList(reservation)
        reservationDao.checkInReservation(firstInLineReservation.id)
        reservation.table = restaurantConfig.assignTable(firstInLineReservation.id)
        return firstInLineReservation
    }

    override fun checkInReservation(reservationId: UUID): Reservation {
        if (restaurantConfig.tableStatus.none { it.table.tableStatus == TableStatus.OPEN }) {
            println("All tables are taken, added to wait list")
            addToWaitList(getReservation(reservationId))
        }
        val reservation = reservationDao.checkInReservation(reservationId)
        reservation.table = restaurantConfig.assignTable(reservationId)
        return reservation
    }

    override fun checkoutReservation(reservationId: UUID): Reservation {
        val reservation = reservationDao.checkoutReservation(reservationId)

        restaurantConfig.unAssignTable(reservationId)
        reservation.table = null

        return reservation
    }
}
