package com.example.accurateuserapp.presentation.adduser

data class AddUserUiState(
    val name: String = "",
    val nameError: String? = null,
    val address: String = "",
    val addressError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val phoneNumber: String = "",
    val phoneNumberError: String? = null,
    val city: String = "",
    val cityError: String? = null,
    val gender: Int = 0, // 0 = Male, 1 = Female
    val cities: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val generalError: String? = null
)