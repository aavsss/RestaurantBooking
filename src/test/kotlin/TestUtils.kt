import model.Reservation
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

object TestUtils {
    val reservations = listOf(
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
            name = "Reunion of Daleks",
            totalNumberOfPeople = 2,
            dayOfTheReservation = LocalDate.of(2023, 12, 1),
            timeOfTheReservation = LocalTime.of(7, 0),
        ),
        Reservation(
            id = UUID.randomUUID(),
            name = "Doctor who anniversary",
            totalNumberOfPeople = 3,
            dayOfTheReservation = LocalDate.of(2023, 12, 2),
            timeOfTheReservation = LocalTime.of(3, 0),
        ),
    )
}
