import booking.Booking
import booking.BookingService
import bookingpolicy.*
import company.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.Duration
import java.time.Instant
import java.time.LocalDate

class HotelBookingAcceptanceTest {

    private val companyDatabase = MemoryDatabase<Int, Company>()
    private val companyRepository = RepositoryImpl(companyDatabase)
    private val companyService = CompanyService(companyRepository)

    private val employeeBookingPolicyDatabase = MemoryDatabase<Int, EmployeeBookingPolicy>()
    private val companyBookingPolicyDatabase = MemoryDatabase<Int, CompanyBookingPolicy>()
    private val employeeBookingPolicyRepository = RepositoryImpl(employeeBookingPolicyDatabase)
    private val companyBookingPolicyRepository = RepositoryImpl(companyBookingPolicyDatabase)
    private val bookingPolicyService = BookingPolicyService(
        employeeBookingPolicyRepository,
        companyBookingPolicyRepository,
        companyRepository
    )

    private val hotelDatabase = MemoryDatabase<Int, Hotel>()
    private val hotelRepository = RepositoryImpl(hotelDatabase)
    private val hotelService: HotelService = HotelService(hotelRepository)

    private val bookingService = BookingService(bookingPolicyService, hotelService)

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
                LocalDate.now(),
                LocalDate.now().plusDays(1)
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
                LocalDate.now(),
                LocalDate.now()
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
                LocalDate.now(),
                LocalDate.now().plusDays(1)
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
                LocalDate.now(),
                LocalDate.now().plusDays(1)
            )
        }

        then { assertThat(result).isEqualTo(Booking.Error.InvalidRoomType) }
    }

    @Test
    fun `employee can book room type according to employee booking policy`() {
        val bookingId = 11
        given {
            `a hotel with one room`(roomType)
            `company added`()
            `an employee added to a company`(companyId, employeeId)
            `an employee booking policy set`()
        }

        val checkIn = LocalDate.now()
        val checkOut = LocalDate.now().plusDays(10)
        val result = whenever {
            bookingService.book(
                employeeId,
                hotelId,
                roomType,
                checkIn,
                checkOut
            )
        }

        then { assertThat(result).isEqualTo(Booking.Success(bookingId, employeeId, hotelId, checkIn, checkOut, Room(number, roomType))) }
    }

    @Test
    fun `booking not allowed when no rooms available for entire booking period`() {
        given {
            `a hotel with one room`(roomType)
            `company added`()
            `an employee added to a company`(companyId, employeeId)
        }

        val checkIn = LocalDate.now()
        val checkOut = LocalDate.now().plusDays(10)
        whenever {
            bookingService.book(
                employeeId,
                hotelId,
                roomType,
                checkIn,
                checkOut
            )
        }

        val result = whenever {
            bookingService.book(
                employeeId,
                hotelId,
                roomType,
                checkIn,
                checkOut
            )
        }

        assertThat(result).isEqualTo(Booking.Error.NoRoomsAvailable)
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
    *
    * if wanted room type is unavailable for the booking period then return roomTypeUnavailable
    * Hotel rooms can be booked many times as long as there are no conflicts with the dates.
    * Given employee can book room when at at least one room type available in booking period THEN Booking allowed
    * Booking can be made within hotel room capacity
    * booking cannot be made at hotel that exceeds hotel room capacity
    * */
}

fun given(block: () -> Unit) = block()
fun <T> whenever(block: () -> T) = block()
fun then(block: () -> Unit) = block()