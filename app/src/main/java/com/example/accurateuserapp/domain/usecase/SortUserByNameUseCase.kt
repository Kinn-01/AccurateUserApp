package com.example.accurateuserapp.domain.usecase

import com.example.accurateuserapp.domain.model.SortOrder
import com.example.accurateuserapp.domain.model.User
import javax.inject.Inject

class SortUserByNameUseCase @Inject constructor() {
    operator fun invoke(users: List<User>, sortOrder: SortOrder): List<User> {
        return when (sortOrder) {
            SortOrder.NAME_ASC -> users.sortedBy { it.name.lowercase() }
            SortOrder.NAME_DESC -> users.sortedByDescending { it.name.lowercase() }
            SortOrder.NONE -> users
        }
    }
}