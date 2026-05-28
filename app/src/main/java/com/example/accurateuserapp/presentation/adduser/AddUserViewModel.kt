package com.example.accurateuserapp.presentation.adduser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accurateuserapp.domain.model.User
import com.example.accurateuserapp.domain.usecase.CreateUserUseCase
import com.example.accurateuserapp.domain.repository.UserRepository
import com.example.accurateuserapp.util.AnalyticsHelper
import com.example.accurateuserapp.util.Resource
import com.example.accurateuserapp.util.isValidEmail
import com.example.accurateuserapp.util.isValidPhoneNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUserViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val userRepository: UserRepository,
    private val analyticsHelper: AnalyticsHelper
) : ViewModel() {

    private val _state = MutableStateFlow(AddUserUiState())
    val state: StateFlow<AddUserUiState> = _state.asStateFlow()

    init {
        analyticsHelper.trackEvent("user_add_started")
        loadCities()
    }

    private fun loadCities() {
        viewModelScope.launch {
            userRepository.getCities().onEach { result ->
                if (result is Resource.Success) {
                    _state.value = _state.value.copy(cities = result.data)
                }
            }.launchIn(this)
        }
    }

    fun onNameChange(value: String) {
        _state.value = _state.value.copy(name = value, nameError = null)
    }

    fun onAddressChange(value: String) {
        _state.value = _state.value.copy(address = value, addressError = null)
    }

    fun onEmailChange(value: String) {
        _state.value = _state.value.copy(email = value, emailError = null)
    }

    fun onPhoneNumberChange(value: String) {
        _state.value = _state.value.copy(phoneNumber = value, phoneNumberError = null)
    }

    fun onCityChange(value: String) {
        _state.value = _state.value.copy(city = value, cityError = null)
    }

    fun onGenderChange(value: Int) {
        _state.value = _state.value.copy(gender = value)
    }

    fun submitForm() {
        if (!validateForm()) {
            analyticsHelper.trackEvent("user_add_failed", mapOf("reason" to "validation_error"))
            return
        }

        _state.value = _state.value.copy(isLoading = true, generalError = null)

        viewModelScope.launch {
            val newUser = User(
                id = "",
                name = _state.value.name.trim(),
                address = _state.value.address.trim(),
                email = _state.value.email.trim(),
                phoneNumber = _state.value.phoneNumber.trim(),
                city = _state.value.city.trim(),
                gender = _state.value.gender,
                isSynced = false
            )

            val result = createUserUseCase(newUser)
            when (result) {
                is Resource.Success -> {
                    analyticsHelper.trackEvent("user_added_successfully", mapOf("name" to newUser.name))
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }
                is Resource.Error -> {
                    analyticsHelper.trackEvent("user_add_failed", mapOf("reason" to (result.message ?: "unknown")))
                    _state.value = _state.value.copy(
                        isLoading = false,
                        generalError = result.message
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        val nameVal = _state.value.name.trim()
        val addressVal = _state.value.address.trim()
        val emailVal = _state.value.email.trim()
        val phoneVal = _state.value.phoneNumber.trim()
        val cityVal = _state.value.city.trim()

        if (nameVal.isEmpty()) {
            _state.value = _state.value.copy(nameError = "Nama lengkap harus diisi")
            isValid = false
        }

        if (addressVal.isEmpty()) {
            _state.value = _state.value.copy(addressError = "Alamat harus diisi")
            isValid = false
        }

        if (emailVal.isEmpty()) {
            _state.value = _state.value.copy(emailError = "Email harus diisi")
            isValid = false
        } else if (!emailVal.isValidEmail()) {
            _state.value = _state.value.copy(emailError = "Format email tidak valid")
            isValid = false
        }

        if (phoneVal.isEmpty()) {
            _state.value = _state.value.copy(phoneNumberError = "Nomor telepon harus diisi")
            isValid = false
        } else if (!phoneVal.isValidPhoneNumber()) {
            _state.value = _state.value.copy(phoneNumberError = "Nomor telepon harus berupa angka (8-15 digit)")
            isValid = false
        }

        if (cityVal.isEmpty()) {
            _state.value = _state.value.copy(cityError = "Kota harus diisi")
            isValid = false
        }

        return isValid
    }
}