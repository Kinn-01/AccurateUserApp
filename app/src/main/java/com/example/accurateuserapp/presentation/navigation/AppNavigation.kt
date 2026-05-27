package com.example.accurateuserapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.accurateuserapp.presentation.adduser.AddUserScreen
import com.example.accurateuserapp.presentation.adduser.AddUserViewModel
import com.example.accurateuserapp.presentation.userlist.UserListScreen
import com.example.accurateuserapp.presentation.userlist.UserListViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.UserList.route
    ) {

        composable(Screen.UserList.route) {
            val viewModel: UserListViewModel = hiltViewModel()
            UserListScreen(
                viewModel = viewModel,
                onAddUserClick = {
                    navController.navigate(Screen.AddUser.route)
                }
            )
        }

        composable(Screen.AddUser.route) {
            val viewModel: AddUserViewModel = hiltViewModel()
            AddUserScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}