package dao.table

import dao.RestaurantConfig
import model.RestaurantBookingStatus
import model.Table
import model.TableStatus

class TableRepo(
    restaurantConfig: RestaurantConfig,
) {
    val tableStatus = mutableListOf<RestaurantBookingStatus>()

    init {
        repeat(restaurantConfig.totalNumberOfTables) {
            tableStatus.add(
                RestaurantBookingStatus(
                    Table(it, TableStatus.OPEN),
                    null,
                ),
            )
        }
    }
}
