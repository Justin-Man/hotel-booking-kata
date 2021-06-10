import booking.Booking
import booking.BookingService
import bookingpolicy.*
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

    private val employeeBookingPolicyDatabase = MemoryDatabase<Int, EmployeeBookingPolicy>()
    private val companyBookingPolicyDatabase = MemoryDatabase<Int, CompanyBookingPolicy>()
    private val employeeBookingPolicyRepository = RepositoryImpl(employeeBookingPolicyDatabase)
    private val companyBookingPolicyRepository = RepositoryImpl(companyBookingPolicyDatabase)
    private val employeeDatabase = MemoryDatabase<Int, Employee>()
    private val employeeRepository = RepositoryImpl(employeeDatabase)
    private val bookingPolicyService = BookingPolicyService(
        employeeBookingPolicyRepository,
        companyBookingPolicyRepository,
        companyRepository,
        employeeRepository
    )

    private val hotelDatabase = MemoryDatabase<Int, Hotel>()
    private val hotelRepository = RepositoryImpl(hotelDatabase)
    private val hotelService: HotelService = HotelService(hotelRepository)

    private val bookingService = BookingService(bookingPolicyService, hotelService)
    val companyAdmin = CompanyAdmin(companyService, bookingService)

    @Test
    fun `employee cannot book room type against employee booking policy`() {
        given {
            `a hotel with two rooms`() // needs two rooms
            `company added`()
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

    @Test
    fun `check out date not set at least one day from check in date results in error`() {
        given {
            `a hotel with one room`(roomType)
            `company added`()
            `an employee added to a company`(companyId, employeeId)
            `an employee booking policy set`()
        }

        val result = whenever {
            bookingService.book(
                employeeId,
                hotelId,
                roomType,
                Date.from(Instant.now()),
                Date.from(Instant.now())
            )
        }

        then { assertThat(result).isEqualTo(Booking.Error.InvalidCheckOutDate) }
    }

    @Test
    fun `booking disallowed when booking room at non-existent hotel`() {
        given {
            `company added`()
            `an employee added to a company`(companyId, employeeId)
            `an employee booking policy set`()
        }

        val result = whenever {
            bookingService.book(
                employeeId,
                hotelId,
                roomType,
                Date.from(Instant.now()),
                Date.from(Instant.now().plus(Duration.ofDays(1)))
            )
        }

        then { assertThat(result).isEqualTo(Booking.Error.InvalidHotel) }
    }

    @Test
    fun `valid room type is provided by the hotel when booking`() {
        given {
            `a hotel with one room`(hotelRoomType)
            `company added`()
            `an employee added to a company`(companyId, employeeId)
            `an employee booking policy set`()
        }

        val result = whenever {
            bookingService.book(
                employeeId,
                hotelId,
                roomType,
                Date.from(Instant.now()),
                Date.from(Instant.now().plus(Duration.ofDays(1)))
            )
        }

        then { assertThat(result).isEqualTo(Booking.Error.InvalidRoomType) }
    }

    private fun `company added`() {
        companyDatabase.table[companyId] = Company(companyId)
    }

    private fun `an employee booking policy set`() {
        bookingPolicyService.setEmployeePolicy(employeeId, listOf(roomType))
    }

    private fun `an employee added to a company`(companyId: Int, employeeId: Int) {
        companyService.addEmployee(companyId, employeeId)
    }

    private fun `a hotel with one room`(roomType: RoomType) {
        hotelDatabase.table[hotelId] = hotel
        hotelService.setRoom(hotelId, number, roomType)
    }

    private fun `a hotel with two rooms`() {
        hotelDatabase.table[hotelId] = hotel
        hotelService.setRoom(hotelId, number, roomType)
        hotelService.setRoom(hotelId, number, hotelRoomType)
    }

    val hotelId = 1
    val hotel = Hotel(hotelId)
    val number = 11
    val roomType = RoomType.StandardSingle
    val hotelRoomType = RoomType.MasterSuite
    val companyId = 100
    val employeeId = 1

    /*
    * valid room type is provided by the hotel when booking
    * Employee can book room type according to employee booking policy
    * Employee can book room type according to company booking policy
    * Given employee can book room when at at least one room type availble in booking period THEN Booking allowed
    * Booking can be made within hotel room capacity
    * booking cannot be made at hotel that exceeds hotel room capacity
    * */
}

fun given(block: () -> Unit) = block()
fun <T> whenever(block: () -> T) = block()
fun then(block: () -> Unit) = block()