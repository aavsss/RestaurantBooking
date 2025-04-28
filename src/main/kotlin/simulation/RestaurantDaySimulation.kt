package simulation

import dao.RestaurantConfig
import dao.order.OrderDaoImpl
import dao.order.OrderRepo
import dao.reservation.ReservationDaoImpl
import dao.reservation.ReservationRepo
import dao.table.TableRepo
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import model.MenuItem
import model.Order
import model.Reservation
import service.order.OrderServiceImpl
import service.reservation.*
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class RestaurantDaySimulation {
    // repos
    val reservationRepo = ReservationRepo()
    val orderRepo = OrderRepo()

    // config
    val restaurantConfig = RestaurantConfig()

    // daos
    val reservationFinder = ReservationFinder(reservationRepo, restaurantConfig)
    val reservationDao = ReservationDaoImpl(reservationFinder, reservationRepo)
    val orderDao = OrderDaoImpl(orderRepo)
    val tableRepo = TableRepo(restaurantConfig)

    // service
    val waitListService = WaitListServiceImpl(reservationDao)
    val tableService = TableServiceImpl(tableRepo, waitListService)
    val reservationDeleteEventHandlerImpl = ReservationDeleteEventHandlerImpl()
    val reservationService = ReservationServiceImpl(
        reservationDao,
        reservationFinder,
        reservationDeleteEventHandlerImpl,
        tableService,
        waitListService,
    )
    val reservationDeleteEventListener = ReservationDeleteEventListener(
        reservationDeleteEventHandlerImpl,
        reservationService,
        waitListService,
    )
    val orderService = OrderServiceImpl(orderDao)

    fun simulate() = runBlocking {
        val action1 = async { action() }
        val action2 = async { action() }
        val action3 = async { action() }
        val action4 = async { action() }
        listOf(
            action1,
            action2,
            action3,
            action4,
        ).awaitAll()
    }

    fun action() {
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
                name = "Anniversary party",
                totalNumberOfPeople = 2,
                dayOfTheReservation = LocalDate.of(2023, 12, 1),
                timeOfTheReservation = LocalTime.of(9, 15),
            ),
        )
        val marvelRivalsReservation = reservationService.createReservation(
            Reservation(
                id = UUID.randomUUID(),
                name = "Marvel rivals party",
                totalNumberOfPeople = 2,
                dayOfTheReservation = LocalDate.of(2023, 12, 1),
                timeOfTheReservation = LocalTime.of(9, 15),
            ),
        )
        val survivalReservation = reservationService.createReservation(
            Reservation(
                id = UUID.randomUUID(),
                name = "Survival party",
                totalNumberOfPeople = 2,
                dayOfTheReservation = LocalDate.of(2023, 12, 1),
                timeOfTheReservation = LocalTime.of(9, 15),
            ),
        )
        val earlyBirdParty = reservationService.createReservation(
            Reservation(
                id = UUID.randomUUID(),
                name = "Early bird party",
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
        println("table status: ${tableService.getTableStatus()}")

        // checkout
        reservationService.checkoutReservation(pizzaPartyReservationId)
        reservationService.checkoutReservation(momoPartyReservationId)
        reservationService.checkoutReservation(anniversaryReservation)
        reservationService.checkoutReservation(marvelRivalsReservation)

        // just divided it because survivalReservation gets hecked in after one reservation checks out
        reservationService.checkoutReservation(survivalReservation)

        println("momo party order total: ${orderRepo.orderList[0].calculateTotal()}")
        println("pizza party order total: ${orderRepo.orderList[1].calculateTotal()}")
        println("table status: ${tableService.getTableStatus()}")
    }
}
