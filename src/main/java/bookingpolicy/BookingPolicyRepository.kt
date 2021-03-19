package bookingpolicy

import RoomType

class BookingPolicyRepository(private val bookingPolicyDatabase: BookingPolicyDatabase) {

    fun add(employeeId: Int, roomTypes: List<RoomType>) {
        bookingPolicyDatabase.employeesWithBookingPolicies[employeeId] = roomTypes
    }

    fun find(employeeId: Int): List<RoomType>? {
        return bookingPolicyDatabase.employeesWithBookingPolicies[employeeId]
    }

    fun findCompanyBookingPolicy(companyId: Int): List<RoomType>? {
        return bookingPolicyDatabase.companyBookingPolicies[companyId]
    }
}