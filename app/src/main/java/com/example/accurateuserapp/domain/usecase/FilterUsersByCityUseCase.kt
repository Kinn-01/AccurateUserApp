package com.example.accurateuserapp.domain.usecase

import com.example.accurateuserapp.domain.model.User
import javax.inject.Inject

class FilterUsersByCityUseCase @Inject constructor() {
    operator fun invoke(users: List<User>, city: String?): List<User> {
        if (city.isNullOrBlank()) return users
        return users.filter { it.city.equals(city, ignoreCase = true) }
    }
}