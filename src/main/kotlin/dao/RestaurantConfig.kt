package dao

import model.RestaurantBookingStatus
import model.Table
import model.TableStatus
import java.time.LocalTime
import java.util.UUID

class RestaurantConfig {
    val totalNumberOfTables: Int = 5
    val numberOfSeatingPerTable: Int = 2
    val startTime: LocalTime = LocalTime.of(4, 0, 0)
    val endTime: LocalTime = LocalTime.of(9, 0, 0)

    val tableStatus = mutableListOf(
        RestaurantBookingStatus(Table(0, TableStatus.OPEN), null),
        RestaurantBookingStatus(Table(1, TableStatus.OPEN), null),
        RestaurantBookingStatus(Table(2, TableStatus.OPEN), null),
        RestaurantBookingStatus(Table(3, TableStatus.OPEN), null),
        RestaurantBookingStatus(Table(4, TableStatus.OPEN), null),
    )

    fun assignTable(reservationId: UUID): Table {
        val existingTables = tableStatus.filter { it.table.tableStatus == TableStatus.OPEN }
        if (existingTables.isEmpty()) throw Exception("All tables are taken")

        existingTables[0].table.tableStatus = TableStatus.EQUIPPED
        existingTables[0].reservationId = reservationId

        return existingTables[0].table
    }

    fun unAssignTable(reservationId: UUID) {
        val assignedTable = tableStatus.firstOrNull { it.reservationId == reservationId }
            ?: throw Exception("Cannot find the assigned table")
        assignedTable.table.tableStatus = TableStatus.OPEN
        assignedTable.reservationId = null
    }
}
