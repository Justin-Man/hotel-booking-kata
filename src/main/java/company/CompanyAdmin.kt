package company

import booking.BookingService

class CompanyAdmin(
    val companyService: CompanyService,
    val bookingService: BookingService
) {

}