import dao.RestaurantConfig
import dao.order.OrderDaoImpl
import dao.order.OrderRepo
import dao.reservation.ReservationDaoImpl
import dao.reservation.ReservationRepo
import model.MenuItem
import model.Order
import model.Reservation
import service.order.OrderServiceImpl
import service.reservation.ReservationDeleteEventHandlerImpl
import service.reservation.ReservationDeleteEventListener
import service.reservation.ReservationFinder
import service.reservation.ReservationServiceImpl
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

fun main(args: Array<String>) {
    // repos
    val reservationRepo = ReservationRepo()
    val orderRepo = OrderRepo()

    // config
    val restaurantConfig = RestaurantConfig()

    // daos
    val reservationFinder = ReservationFinder(reservationRepo, restaurantConfig)
    val reservationDao = ReservationDaoImpl(reservationFinder, reservationRepo)
    val orderDao = OrderDaoImpl(orderRepo)

    // service
    val reservationDeleteEventHandlerImpl = ReservationDeleteEventHandlerImpl()
    val reservationDeleteEventListener = ReservationDeleteEventListener(reservationRepo, reservationDeleteEventHandlerImpl)
    val reservationService = ReservationServiceImpl(reservationDao, reservationFinder, restaurantConfig, reservationDeleteEventHandlerImpl)
    val orderService = OrderServiceImpl(orderDao)

    // simulation
    val pizzaPartyReservationId = reservationService.createReservation(
        Reservation(
            id = UUID.randomUUID(),
            name = "Pizza party",
            totalNumberOfPeople = 2,
            dayOfTheReservation = LocalDate.of(2023, 12, 1),
            timeOfTheReservation = LocalTime.of(9, 15),
        ),
    )
    val momoPartyReservationId = reservationService.createReservation(
        Reservation(
            id = UUID.randomUUID(),
            name = "Momo party",
            totalNumberOfPeople = 2,
            dayOfTheReservation = LocalDate.of(2023, 12, 1),
            timeOfTheReservation = LocalTime.of(9, 15),
        ),
    )
    val anniversaryReservation = reservationService.createReservation(
        Reservation(
            id = UUID.randomUUID(),
            name = "Momo party",
            totalNumberOfPeople = 2,
            dayOfTheReservation = LocalDate.of(2023, 12, 1),
            timeOfTheReservation = LocalTime.of(9, 15),
        ),
    )
    val marvelRivalsReservation = reservationService.createReservation(
        Reservation(
            id = UUID.randomUUID(),
            name = "Momo party",
            totalNumberOfPeople = 2,
            dayOfTheReservation = LocalDate.of(2023, 12, 1),
            timeOfTheReservation = LocalTime.of(9, 15),
        ),
    )
    val survivalReservation = reservationService.createReservation(
        Reservation(
            id = UUID.randomUUID(),
            name = "Momo party",
            totalNumberOfPeople = 2,
            dayOfTheReservation = LocalDate.of(2023, 12, 1),
            timeOfTheReservation = LocalTime.of(9, 15),
        ),
    )
    val earlyBirdParty = reservationService.createReservation(
        Reservation(
            id = UUID.randomUUID(),
            name = "Momo party",
            totalNumberOfPeople = 2,
            dayOfTheReservation = LocalDate.of(2023, 12, 1),
            timeOfTheReservation = LocalTime.of(4, 0),
        ),
    )

    // check in reservation for tables to be assigned
    reservationService.checkInReservation(earlyBirdParty)

    // todo: what to do if people haven't checkout out after their allocated time
    reservationService.checkInReservation(pizzaPartyReservationId)
    reservationService.checkInReservation(momoPartyReservationId)
    reservationService.checkInReservation(anniversaryReservation)
    reservationService.checkInReservation(marvelRivalsReservation)
    reservationService.checkInReservation(survivalReservation)

    orderService.placeOrder(
        Order(
            id = UUID.randomUUID(),
            reservationId = momoPartyReservationId,
            menuItems = mutableListOf(MenuItem.MOMO),
        ),
    )

    orderService.placeOrder(
        Order(
            id = UUID.randomUUID(),
            reservationId = pizzaPartyReservationId,
            menuItems = mutableListOf(MenuItem.PIZZA),
        ),
    )

    // State
    println("table status: ${restaurantConfig.tableStatus}")

    // checkout
    reservationService.checkoutReservation(pizzaPartyReservationId)
    reservationService.checkoutReservation(momoPartyReservationId)
    reservationService.checkoutReservation(anniversaryReservation)
    reservationService.checkoutReservation(marvelRivalsReservation)
    reservationService.checkoutReservation(survivalReservation)

    println("momo party order total: ${orderRepo.orderList[0].calculateTotal()}")
    println("pizza party order total: ${orderRepo.orderList[1].calculateTotal()}")
    println("table status: ${restaurantConfig.tableStatus}")
}
