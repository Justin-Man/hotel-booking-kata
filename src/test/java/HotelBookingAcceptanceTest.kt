import booking.Booking
import booking.BookingService
import bookingpolicy.BookingPolicyInMemoryDatabaseImpl
import bookingpolicy.BookingPolicyRepository
import bookingpolicy.BookingPolicyService
import company.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.Duration
import java.time.Instant
import java.util.*

class HotelBookingAcceptanceTest {

    private val companyDatabase = MemoryDatabase<Int, Company>()
    private val companyRepository = RepositoryImpl(companyDatabase)
    private val companyService = CompanyService(companyRepository)
    private val bookingPolicyDatabase = BookingPolicyInMemoryDatabaseImpl()
    private val bookingPolicyRepository = BookingPolicyRepository(bookingPolicyDatabase)
    private val bookingPolicyService = BookingPolicyService(bookingPolicyRepository, companyRepository)
    private val hotelDatabase = MemoryDatabase<Int, Hotel>()
    private val hotelRepository = RepositoryImpl(hotelDatabase)
    private val hotelService: HotelService = HotelService(hotelRepository)
    private val bookingService = BookingService(bookingPolicyService)
    val companyAdmin = CompanyAdmin(companyService, bookingService)

    @Test
    fun `employee cannot book room type against employee policy`() {
        given {
            `a hotel with one room`()
            `an employee added to a company`(companyId, employeeId)
            `an employee booking policy set`()
        }

        val result = whenever {
            bookingService.book(
                employeeId,
                hotelId,
                hotelRoomType,
                Date.from(Instant.now()),
                Date.from(Instant.now().plus(Duration.ofDays(1)))
            )
        }

        then {
            assertThat(result).isEqualTo(Booking.Error.AgainstEmployeePolicy)
        }
    }

    private fun `an employee booking policy set`() {
        bookingPolicyService.setEmployeePolicy(employeeId, listOf(roomType))
    }

    private fun `an employee added to a company`(companyId: Int, employeeId: Int) {
        companyService.addEmployee(companyId, employeeId)
    }

    private fun `a hotel with one room`() {
        hotelDatabase.table[hotelId] = hotel
        hotelService.setRoom(hotelId, number, hotelRoomType)
    }

    val hotelId = 1
    val hotel = Hotel(hotelId)
    val number = 11
    val roomType = RoomType.standardSingle
    val hotelRoomType = RoomType.masterSuite
    val companyId = 100
    val employeeId = 1

    /*
    * GIVEN hotel exists and room type available and company employee added WHEN disallowed room type booked THEN booking disallowed error
    * GIVEN hotel exists and room type available and company employee added WHEN room booked with invalid check out date THEN booking invalid checkout date error
    * GIVEN hotel does not exists WHEN room booked THEN booking disallowed
    *
    *
    * */
}

fun given(block: () -> Unit) = block()
fun <T> whenever(block: () -> T) = block()
fun then(block: () -> Unit) = block()