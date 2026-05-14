package models

data class ChatRoomModel(
    val ChatRoomId: String = "",
    val userIds: ArrayList<String> = ArrayList(),
    val lastMessageTimestamp: Long = System.currentTimeMillis(),
    val lastMessage: String = ""
)