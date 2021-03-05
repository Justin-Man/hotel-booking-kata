class Hotel(val hotelId: Int) {

    private val rooms = mutableListOf<Room>()

    fun setRoom(number: Int, roomType: RoomType) {
        rooms.find { room ->  room.number == number }?.setRoomType(roomType)
            ?: rooms.add(Room(number, roomType))
    }

    fun updateRoom() {

    }
}