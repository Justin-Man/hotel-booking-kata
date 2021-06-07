package bookingpolicy

import Repository
import RoomType
import company.Company


class BookingPolicyService(
    private val employeeBookingPolicyRepository: Repository<Int, EmployeeBookingPolicy>,
    private val companyBookingPolicyRepository: Repository<Int, CompanyBookingPolicy>,
    private val companyRepository: Repository<Int, Company>,
    private val employeeRepository: Repository<Int, Employee>
) {

    fun setEmployeePolicy(employeeId: Int, roomTypes: List<RoomType>) {
        employeeBookingPolicyRepository.add(EmployeeBookingPolicy(employeeId, roomTypes))
    }

    fun isBookingAllowed(employeeId: Int, roomType: RoomType): Boolean {

        val companyId = employeeRepository.find(employeeId)?.companyId
        val employeePolicy = employeeBookingPolicyRepository.find(employeeId)
        if (employeePolicy?.allowedRoomTypes?.contains(roomType) == true) {
            return true
        }

        val companyBookingPolicy = companyId?.let { companyBookingPolicyRepository.find(it) }
        if (companyBookingPolicy?.allowedRoomTypes?.contains(roomType) == true) {
            return true
        }

        if (employeePolicy?.allowedRoomTypes.isNullOrEmpty() && companyBookingPolicy?.allowedRoomTypes.isNullOrEmpty()) {
            return true
        }

        return false
    }

    fun setCompanyPolicy(companyId: Int, roomtypes: List<RoomType>) {

        companyBookingPolicyRepository.add(CompanyBookingPolicy(companyId, roomtypes))
    }


}
