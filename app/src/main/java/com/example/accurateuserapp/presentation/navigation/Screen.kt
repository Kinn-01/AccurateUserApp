package com.example.accurateuserapp.presentation.navigation

sealed class Screen(val route: String) {
    object UserList : Screen("user_list")
    object AddUser : Screen("add_user")
}