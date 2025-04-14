package model

import java.util.UUID

data class RestaurantBookingStatus(
    val table: Table,
    var reservationId: UUID?,
)
