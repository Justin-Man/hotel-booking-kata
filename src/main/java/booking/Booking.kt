package booking

sealed class Booking {
    sealed class Error : Booking() {
        object AgainstEmployeePolicy : Error()
    }
}
