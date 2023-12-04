package dao

import model.Reservation
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import service.ReservationFinder
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import kotlin.test.assertNotNull

class ReservationFinderTest {
    private lateinit var reservationRepo: ReservationRepo
    private lateinit var reservationFinder: ReservationFinder

    @BeforeEach
    fun setup() {
        this.reservationRepo = ReservationRepo()
        this.reservationRepo.reservationSet.addAll(
            listOf(
                Reservation(
                    id = UUID.randomUUID(),
                    name = "11 month anniversary",
                    totalNumberOfPeople = 2,
                    dayOfTheReservation = LocalDate.of(2023, 12, 1),
                    timeOfTheReservation = LocalTime.of(9, 15),
                ),
                Reservation(
                    id = UUID.randomUUID(),
                    name = "Reunion",
                    totalNumberOfPeople = 2,
                    dayOfTheReservation = LocalDate.of(2023, 12, 1),
                    timeOfTheReservation = LocalTime.of(6, 0),
                ),
                Reservation(
                    id = UUID.randomUUID(),
                    name = "Doctor who anniversary",
                    totalNumberOfPeople = 3,
                    dayOfTheReservation = LocalDate.of(2023, 12, 2),
                    timeOfTheReservation = LocalTime.of(3, 0),
                ),
            )
        )
        this.reservationFinder = ReservationFinder(reservationRepo)
    }

    @Test
    fun shouldPass() {
        assertNotNull(reservationFinder)
    }

    @Test
    fun createReservation_bookingWithin90Mins_CantBook() {
        val reservationToAdd = Reservation(
            id = UUID.randomUUID(),
            name = "Cyberman re-awakening",
            totalNumberOfPeople = 2,
            dayOfTheReservation = LocalDate.of(2023, 12, 1),
            timeOfTheReservation = LocalTime.of(9, 30)
        )
    }
}