package com.example.accurateuserapp.domain.usecase

import com.example.accurateuserapp.domain.model.User
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor() {
    operator fun invoke(users: List<User>, query: String): List<User> {
        if (query.isBlank()) return users
        return users.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.email.contains(query, ignoreCase = true) ||
                    it.phoneNumber.contains(query, ignoreCase = true)
        }
    }
}