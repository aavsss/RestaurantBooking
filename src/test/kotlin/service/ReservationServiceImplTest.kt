package service

import TestUtils
import dao.RestaurantConfig
import dao.reservation.ReservationDao
import dao.reservation.ReservationDaoImpl
import dao.reservation.ReservationRepo
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import model.Reservation
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import service.reservation.ReservationDeleteEventHandlerImpl
import service.reservation.ReservationDeleteEventListener
import service.reservation.ReservationFinder
import service.reservation.ReservationServiceImpl
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class ReservationServiceImplTest {
    private lateinit var reservationRepo: ReservationRepo
    private lateinit var restaurantConfig: RestaurantConfig
    private lateinit var reservationDao: ReservationDao
    private lateinit var reservationFinder: ReservationFinder
    private lateinit var reservationServiceImpl: ReservationServiceImpl
    private lateinit var reservationDeleteEventHandlerImpl: ReservationDeleteEventHandlerImpl

    private val waitListUuid = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        reservationRepo = ReservationRepo()
        restaurantConfig = RestaurantConfig()
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
        reservationFinder = ReservationFinder(reservationRepo, restaurantConfig)
        reservationDao = ReservationDaoImpl(reservationFinder, reservationRepo)
        reservationDeleteEventHandlerImpl = ReservationDeleteEventHandlerImpl()
        reservationServiceImpl = ReservationServiceImpl(reservationDao, reservationFinder, restaurantConfig, reservationDeleteEventHandlerImpl)
    }

    @Test
    fun deleteReservation_publishesEvent() = runBlocking {
        val uuidToDelete = reservationRepo.reservationSet.first()
        ReservationDeleteEventListener(reservationRepo, reservationDeleteEventHandlerImpl)
        reservationServiceImpl.deleteReservation(uuidToDelete.id)

        delay(1500)
        Assertions.assertNotNull(
            reservationRepo.reservationSet.firstOrNull {
                it.id == waitListUuid
            },
        )
    }
}
