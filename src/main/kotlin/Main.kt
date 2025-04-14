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
    val reservationService = ReservationServiceImpl(reservationDao, reservationFinder, reservationDeleteEventHandlerImpl)
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

    orderService.placeOrder(
        Order(
            id = UUID.randomUUID(),
            reservationId = momoPartyReservationId,
            menuItems = mutableListOf(MenuItem.MOMO),
            tableId = restaurantConfig.assignTable(momoPartyReservationId).id,
        ),
    )

    orderService.placeOrder(
        Order(
            id = UUID.randomUUID(),
            reservationId = pizzaPartyReservationId,
            menuItems = mutableListOf(MenuItem.PIZZA),
            tableId = restaurantConfig.assignTable(pizzaPartyReservationId).id,
        ),
    )

    // State
    println("table status: ${restaurantConfig.tableStatus}")

    println("momo party order total: ${orderRepo.orderList[0].calculateTotal()}")
    println("pizza party order total: ${orderRepo.orderList[1].calculateTotal()}")
}
