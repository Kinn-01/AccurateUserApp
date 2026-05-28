package com.example.accurateuserapp.presentation.userlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.accurateuserapp.R
import com.example.accurateuserapp.presentation.components.EmptyState
import com.example.accurateuserapp.presentation.components.FilterChips
import com.example.accurateuserapp.presentation.components.LoadingIndicator
import com.example.accurateuserapp.presentation.components.SearchBar
import com.example.accurateuserapp.presentation.components.SortMenu
import com.example.accurateuserapp.presentation.components.UserCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    onAddUserClick: () -> Unit,
    viewModel: UserListViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.accurate_users),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    SortMenu(
                        selectedSortOrder = state.selectedSortOrder,
                        onSortOrderSelected = { viewModel.onSortOrderChanged(it) }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddUserClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_user)
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search field
            SearchBar(
                query = state.searchQuery,
                onQueryChange = { viewModel.onSearchQueryChanged(it) }
            )

            // Scrollable city filter chips
            FilterChips(
                cities = state.cities,
                selectedCity = state.selectedCity,
                onCitySelected = { viewModel.onCitySelected(it) }
            )

            // Pull-to-refresh box containing loaded users or empty states
            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = { viewModel.loadUsers(forceRefresh = true) },
                state = pullToRefreshState,
                modifier = Modifier.weight(1f)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    when {
                        state.isLoading && state.users.isEmpty() -> {
                            LoadingIndicator()
                        }
                        state.filteredUsers.isEmpty() -> {
                            val emptyMsg = if (state.searchQuery.isNotEmpty() || state.selectedCity != null) {
                                stringResource(R.string.user_not_found)
                            } else {
                                stringResource(R.string.user_empty)
                            }
                            EmptyState(message = emptyMsg)
                        }
                        else -> {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(state.filteredUsers, key = { it.id }) { user ->
                                    UserCard(user = user)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}