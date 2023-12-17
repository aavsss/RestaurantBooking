package service

import TestUtils
import dao.ReservationDao
import dao.ReservationDaoImpl
import dao.ReservationRepo
import model.Reservation
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class ReservationServiceImplTest {
    private lateinit var reservationRepo: ReservationRepo
    private lateinit var reservationDao: ReservationDao
    private lateinit var reservationFinder: ReservationFinder
    private lateinit var reservationServiceImpl: ReservationServiceImpl

    private val waitListUuid = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        reservationRepo = ReservationRepo()
        this.reservationRepo.reservationSet.addAll(TestUtils.reservations)
        this.reservationRepo.waitList.addAll(
            listOf(
                Reservation(
                    id = waitListUuid,
                    name = "Florida weekend out",
                    totalNumberOfPeople = 3,
                    dayOfTheReservation = LocalDate.of(2023, 12, 1),
                    timeOfTheReservation = LocalTime.of(9, 15),
                ),
            ),
        )
        reservationFinder = ReservationFinder(reservationRepo)
        reservationDao = ReservationDaoImpl(reservationFinder, reservationRepo)
        reservationServiceImpl = ReservationServiceImpl(reservationDao, reservationFinder)
    }

    @Test
    fun deleteReservation_publishesEvent() {
        val uuidToDelete = reservationRepo.reservationSet.first()
        val reservationEventListener = ReservationEventListener(reservationRepo)
        reservationEventListener.startListening()
        val deletedReservationUuid = reservationServiceImpl.deleteReservation(uuidToDelete.id)

        Assertions.assertNotNull(
            reservationRepo.reservationSet.firstOrNull {
                it.id == waitListUuid
            },
        )
    }
}
