package com.example.accurateuserapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.accurateuserapp.presentation.navigation.AppNavigation
import com.example.accurateuserapp.ui.theme.AccurateUserAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AccurateUserAppTheme {
                AppNavigation()
            }
        }
    }
}