package models

data class Users(
    val uid: String = "",
    val phoneNumber: String = "",
    val username: String = "",
    val status: String = "Offline" // Default status
)