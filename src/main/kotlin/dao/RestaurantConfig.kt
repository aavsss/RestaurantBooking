package dao

import java.time.LocalTime

class RestaurantConfig {
    val totalNumberOfTables: Int = 20
    val numberOfSeatingPerTable: Int = 2
    val startTime: LocalTime = LocalTime.of(4, 0, 0)
    val endTime: LocalTime = LocalTime.of(9, 0, 0)
}
