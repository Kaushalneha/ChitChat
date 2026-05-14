package models

data class MessageModel(
    val message: String = "",
    val senderId: String = "",
    val timestamp: Long = System.currentTimeMillis()
)