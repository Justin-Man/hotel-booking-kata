package bookingpolicy

import RoomType
import company.CompanyRepository

class BookingPolicyService(
    private val bookingPolicyRepository: BookingPolicyRepository,
    private val companyRepository: CompanyRepository
) {

    fun setEmployeePolicy(employeeId: Int, roomTypes: List<RoomType>) {
        bookingPolicyRepository.add(employeeId, roomTypes)
    }

    fun isBookingAllowed(employeeId: Int, roomType: RoomType): Boolean {

        val companyId = companyRepository.findCompany(employeeId)
        val employeePolicy = bookingPolicyRepository.find(employeeId)
        if (employeePolicy?.contains(roomType) == true) {
            return true
        }

        val companyBookingPolicy = bookingPolicyRepository.findCompanyBookingPolicy(companyId)
        if (companyBookingPolicy?.contains(roomType) == true) {
            return true
        }
        if (employeePolicy.isNullOrEmpty() && companyBookingPolicy.isNullOrEmpty()) {
            return true
        }

        return false
    }
}
