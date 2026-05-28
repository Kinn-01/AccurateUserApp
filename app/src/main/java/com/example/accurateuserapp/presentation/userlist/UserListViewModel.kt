package com.example.accurateuserapp.presentation.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accurateuserapp.domain.model.SortOrder
import com.example.accurateuserapp.domain.usecase.FilterUsersByCityUseCase
import com.example.accurateuserapp.domain.usecase.GetUsersUseCase
import com.example.accurateuserapp.domain.usecase.SearchUsersUseCase
import com.example.accurateuserapp.domain.usecase.SortUserByNameUseCase
import com.example.accurateuserapp.domain.repository.UserRepository
import com.example.accurateuserapp.util.AnalyticsHelper
import com.example.accurateuserapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val searchUsersUseCase: SearchUsersUseCase,
    private val sortUserByNameUseCase: SortUserByNameUseCase,
    private val filterUsersByCityUseCase: FilterUsersByCityUseCase,
    private val userRepository: UserRepository,
    private val analyticsHelper: AnalyticsHelper
) : ViewModel() {

    private val _state = MutableStateFlow(UserListUiState())
    val state: StateFlow<UserListUiState> = _state.asStateFlow()

    init {
        analyticsHelper.trackEvent("user_list_screen_viewed")
        loadUsers(forceRefresh = false)
        loadCities()
    }

    fun loadUsers(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            userRepository.getUsers(forceRefresh).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = !forceRefresh,
                            isRefreshing = forceRefresh,
                            error = null
                        )
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            users = result.data,
                            isLoading = false,
                            isRefreshing = false,
                            error = null
                        )
                        applyFilterAndSort()
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = result.message
                        )
                    }
                }
            }.launchIn(this)
        }
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

    fun onSearchQueryChanged(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
        analyticsHelper.trackEvent("user_searched", mapOf("query" to query))
        applyFilterAndSort()
    }

    fun onCitySelected(city: String?) {
        _state.value = _state.value.copy(selectedCity = city)
        analyticsHelper.trackEvent("user_filtered_by_city", mapOf("city" to (city ?: "all")))
        applyFilterAndSort()
    }

    fun onSortOrderChanged(sortOrder: SortOrder) {
        _state.value = _state.value.copy(selectedSortOrder = sortOrder)
        analyticsHelper.trackEvent("user_sorted", mapOf("order" to sortOrder.name))
        applyFilterAndSort()
    }

    private fun applyFilterAndSort() {
        val currentUsers = _state.value.users

        // 1. Search filter
        var processed = searchUsersUseCase(currentUsers, _state.value.searchQuery)

        // 2. City filter
        processed = filterUsersByCityUseCase(processed, _state.value.selectedCity)

        // 3. Sort
        processed = sortUserByNameUseCase(processed, _state.value.selectedSortOrder)

        _state.value = _state.value.copy(filteredUsers = processed)
    }
}