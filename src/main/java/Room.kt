data class Room(val number : Int, private var roomType: RoomType) {

    fun setRoomType(roomType: RoomType) {
        this.roomType = roomType
    }

    fun getRoomType() = this.roomType
}
