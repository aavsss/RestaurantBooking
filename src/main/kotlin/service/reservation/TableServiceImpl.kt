package service.reservation

import dao.table.TableRepo
import model.RestaurantBookingStatus
import model.Table
import model.TableStatus
import java.util.*

class TableServiceImpl(
    val tableRepo: TableRepo,
    val waitListService: WaitListService,
) : TableService {

    override fun assignTable(reservationId: UUID): Table {
        val existingTables = tableRepo.tableStatus.filter { it.table.tableStatus == TableStatus.OPEN }
        if (existingTables.isEmpty()) {
            waitListService.addToWaitList(reservationId)
        }

        existingTables[0].table.tableStatus = TableStatus.EQUIPPED
        existingTables[0].reservationId = reservationId

        return existingTables[0].table
    }

    override fun unAssignTable(reservationId: UUID) {
        val assignedTable = tableRepo.tableStatus.firstOrNull { it.reservationId == reservationId }
            ?: throw Exception("Cannot find the assigned table")
        assignedTable.table.tableStatus = TableStatus.OPEN
        assignedTable.reservationId = null
    }

    override fun getTableStatus(): MutableList<RestaurantBookingStatus> {
        return tableRepo.tableStatus
    }
}
