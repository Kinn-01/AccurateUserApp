package com.example.accurateuserapp.domain.model

data class UserFilter(
    val searchQuery: String = "",
    val selectedCity: String? = null,
    val sortBy: SortOrder = SortOrder.NONE
)

enum class SortOrder {
    NONE,
    NAME_ASC,
    NAME_DESC
}