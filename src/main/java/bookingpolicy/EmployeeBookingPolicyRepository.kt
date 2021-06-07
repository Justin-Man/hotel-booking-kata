package bookingpolicy

import Database
import RoomType

class EmployeeBookingPolicyRepository(
    private val EmployeeBookingPolicyDatabase: Database<Int, List<RoomType>>,
) {

    fun add(employeeId: Int, roomTypes: List<RoomType>) {
        EmployeeBookingPolicyDatabase.table[employeeId] = roomTypes
    }

    fun find(employeeId: Int): List<RoomType>? {
        return EmployeeBookingPolicyDatabase.table[employeeId]
    }
}