package com.example.accurateuserapp.presentation.userlist

import com.example.accurateuserapp.domain.model.SortOrder
import com.example.accurateuserapp.domain.model.User

data class UserListUiState(
    val users: List<User> = emptyList(),
    val filteredUsers: List<User> = emptyList(),
    val cities: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedCity: String? = null,
    val selectedSortOrder: SortOrder = SortOrder.NONE,
    val isRefreshing: Boolean = false
)