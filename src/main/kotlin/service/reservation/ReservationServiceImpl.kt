package service.reservation

import dao.reservation.ReservationDao
import kotlinx.coroutines.runBlocking
import model.Reservation
import model.ReservationStatus
import model.TableStatus
import java.time.LocalDate
import java.util.*

class ReservationServiceImpl(
    private val reservationDao: ReservationDao,
    private val reservationFinder: ReservationFinder,
    private val reservationDeleteEventHandlerImpl: ReservationDeleteEventHandlerImpl,
    private val tableService: TableService,
    private val waitListService: WaitListService,
) : ReservationService {

    override fun createReservation(reservation: Reservation, addToWaitList: Boolean): UUID {
        if (!reservationFinder.isReservationValidToUpsert(reservation)) {
            val alternateTimes = reservationFinder.findAlternateDates(reservation.dayOfTheReservation)
            if (addToWaitList) {
                reservationDao.addToWaitList(
                    reservation.copy(
                        status = ReservationStatus.WAITING_TO_BE_CHECKED_IN,
                    ),
                )
                println("Added to wait list.")
                return reservation.id
            }
            throw Exception("Time already booked. Pick some other time: $alternateTimes")
        }
        return reservationDao.createReservation(reservation)
    }

    override fun updateReservation(reservationId: UUID, updatedReservation: Reservation): UUID {
        return reservationDao.updateReservation(reservationId, updatedReservation)
    }

    override fun deleteReservation(reservationId: UUID): UUID? = runBlocking {
        return@runBlocking try {
            val deletedReservation = reservationDao.deleteReservation(reservationId)
            // todo: event driven causes race condition issues
//            reservationDeleteEventHandlerImpl.publish(reservationId)
            removeFromWaitListAndCheckIn(
                waitListService.getWaitList().first {
                    it.status == ReservationStatus.WAITING_TO_BE_CHECKED_IN
                }
            )
            deletedReservation.id
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

    override fun removeFromWaitListAndCheckIn(reservation: Reservation): Reservation {
        val firstInLineReservation = reservationDao.removeFromWaitList(reservation)
        reservationDao.checkInReservation(firstInLineReservation.id)
        reservation.table = tableService.assignTable(firstInLineReservation.id)
        return firstInLineReservation
    }

    override fun checkInReservation(reservationId: UUID): Reservation? {
        if (tableService.getTableStatus().none { it.table.tableStatus == TableStatus.OPEN }) {
            println("All tables are taken, added to wait list for reservation id $reservationId")
            waitListService.addToWaitList(reservationId)
            return null
        }
        val reservation = reservationDao.checkInReservation(reservationId)
        reservation.table = tableService.assignTable(reservationId)
        return reservation
    }

    override fun checkoutReservation(reservationId: UUID): Reservation {
        val reservation = reservationDao.checkoutReservation(reservationId)
        reservationDao.moveToPastReservationSet(reservation)
        deleteReservation(reservationId)
        tableService.unAssignTable(reservationId)
        reservation.table = null

        return reservation
    }
}
