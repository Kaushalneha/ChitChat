package models

data class RecentChatModel(
    val otherUserId: String = "",
    val lastMessage: String = "",
    val lastMessageTimestamp: Long = 0,
    val chatRoomId: String = ""
)