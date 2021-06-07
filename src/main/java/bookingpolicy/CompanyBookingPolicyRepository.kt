package bookingpolicy

import Database
import RoomType

class CompanyBookingPolicyRepository(
    private val companyBookingPolicyDatabase: Database<Int, List<RoomType>>
) {
    fun findCompanyBookingPolicy(companyId: Int): List<RoomType>? {
        return companyBookingPolicyDatabase.table[companyId]
    }

    fun add(companyId: Int, roomtypes: List<RoomType>) {
        companyBookingPolicyDatabase.table[companyId] = roomtypes
    }
}
