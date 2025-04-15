package simulation

import dao.RestaurantConfig
import dao.order.OrderDaoImpl
import dao.order.OrderRepo
import dao.reservation.ReservationDaoImpl
import dao.reservation.ReservationRepo
import model.Reservation
import service.order.OrderServiceImpl
import service.reservation.ReservationDeleteEventHandlerImpl
import service.reservation.ReservationDeleteEventListener
import service.reservation.ReservationFinder
import service.reservation.ReservationServiceImpl
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

    // service
    val reservationDeleteEventHandlerImpl = ReservationDeleteEventHandlerImpl()
    val reservationDeleteEventListener = ReservationDeleteEventListener(reservationRepo, reservationDeleteEventHandlerImpl)
    val reservationService = ReservationServiceImpl(reservationDao, reservationFinder, restaurantConfig, reservationDeleteEventHandlerImpl)
    val orderService = OrderServiceImpl(orderDao)
}