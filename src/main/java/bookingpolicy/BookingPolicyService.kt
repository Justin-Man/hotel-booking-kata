package bookingpolicy

import Repository
import RoomType
import company.Company


class BookingPolicyService(
    private val bookingPolicyRepository: BookingPolicyRepository,
    private val companyRepository: Repository<Int, Company>
) {

    fun setEmployeePolicy(employeeId: Int, roomTypes: List<RoomType>) {
        bookingPolicyRepository.add(employeeId, roomTypes)
    }

    fun isBookingAllowed(employeeId: Int, roomType: RoomType): Boolean {

        val companyId = companyRepository.find(employeeId)?.id
        val employeePolicy = bookingPolicyRepository.find(employeeId)
        if (employeePolicy?.contains(roomType) == true) {
            return true
        }

        val companyBookingPolicy = companyId?.let { bookingPolicyRepository.findCompanyBookingPolicy(it) }
        if (companyBookingPolicy?.contains(roomType) == true) {
            return true
        }
        if (employeePolicy.isNullOrEmpty() && companyBookingPolicy.isNullOrEmpty()) {
            return true
        }

        return false
    }
}
