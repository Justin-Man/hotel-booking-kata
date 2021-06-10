package booking

sealed class Booking {
    sealed class Error : Booking() {
        object AgainstEmployeePolicy : Error()
        object InvalidCheckOutDate : Error()
        object InvalidHotel : Error()
        object InvalidRoomType : Error()
    }
}
