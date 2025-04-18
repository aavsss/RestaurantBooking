package dao

import TestUtils
import dao.reservation.ReservationRepo
import model.Reservation
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import service.reservation.ReservationFinder
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ReservationFinderTest {
    private lateinit var reservationRepo: ReservationRepo
    private lateinit var restaurantConfig: RestaurantConfig
    private lateinit var reservationFinder: ReservationFinder

    @BeforeEach
    fun setup() {
        this.reservationRepo = ReservationRepo()
        this.restaurantConfig = RestaurantConfig()
        this.reservationRepo.reservationSet.addAll(
            TestUtils.reservations,
        )
        this.reservationFinder = ReservationFinder(reservationRepo, restaurantConfig)
    }

    @Test
    fun createReservation_bookingWithin90Mins_tablesAvailable_canBook() {
        val reservationToAdd = Reservation(
            id = UUID.randomUUID(),
            name = "Cyberman re-awakening",
            totalNumberOfPeople = 2,
            dayOfTheReservation = LocalDate.of(2023, 12, 1),
            timeOfTheReservation = LocalTime.of(6, 30),
        )
        val actual = reservationFinder.isReservationValidToUpsert(reservationToAdd)
        assertTrue(actual)
    }

    @Test
    fun createReservation_bookingWithin90Mins_tablesRequiredExceedTotalAvailable_cantBook() {
        val reservationToAdd = Reservation(
            id = UUID.randomUUID(),
            name = "Cyberman re-awakening 2",
            totalNumberOfPeople = 10,
            dayOfTheReservation = LocalDate.of(2023, 12, 1),
            timeOfTheReservation = LocalTime.of(6, 30),
        )
        val actual = reservationFinder.isReservationValidToUpsert(reservationToAdd)
        assertFalse(actual)
    }

    @Test
    fun createReservation_bookingWithin90Mins_tablesRequiredDoNotExceedTotalAvailable_cantBook() {
        val reservationToAdd = Reservation(
            id = UUID.randomUUID(),
            name = "Cyberman re-awakening 2",
            totalNumberOfPeople = 4,
            dayOfTheReservation = LocalDate.of(2023, 12, 1),
            timeOfTheReservation = LocalTime.of(6, 30),
        )
        val actual = reservationFinder.isReservationValidToUpsert(reservationToAdd)
        assertTrue(actual)
    }

    @Test
    fun createReservation_bookingDoesntOverlap_tablesExceedMaxCapacity_cantBook() {
        val reservationToAdd = Reservation(
            id = UUID.randomUUID(),
            name = "Cyberman re-awakening 2",
            totalNumberOfPeople = 12,
            dayOfTheReservation = LocalDate.of(2023, 12, 3),
            timeOfTheReservation = LocalTime.of(6, 30),
        )
        val actual = reservationFinder.isReservationValidToUpsert(reservationToAdd)
        assertFalse(actual)
    }

    @Test
    fun createReservation_bookingOverlaps_oddNumberOfPeople_tablesExceedMaxCapacity_cantBook() {
        val reservationToAdd = Reservation(
            id = UUID.randomUUID(),
            name = "Cyberman re-awakening 2",
            totalNumberOfPeople = 7,
            dayOfTheReservation = LocalDate.of(2023, 12, 1),
            timeOfTheReservation = LocalTime.of(6, 30),
        )
        val actual = reservationFinder.isReservationValidToUpsert(reservationToAdd)
        assertFalse(actual)
    }

    @Test
    fun findAlternateDates_listsDatesAsExpected_defaultData() {
        val dates = reservationFinder.findAlternateDates(LocalDate.of(2023, 12, 1))
        assertEquals(1, dates.size)
    }
}
