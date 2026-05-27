package com.example.accurateuserapp.domain.model

data class User(
    val id: String = "",
    val name: String,
    val address: String,
    val email: String,
    val phoneNumber: String,
    val city: String,
    val gender: Int, // 0 = Male, 1 = Female
    val isSynced: Boolean = true
)