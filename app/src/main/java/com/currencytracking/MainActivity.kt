package com.currencytracking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.currencytracking.presentation.screens.MainScreen
import com.currencytracking.ui.theme.CurrencyTrackingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyTrackingTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.MainScreen.route) {
                        composable(route = Screen.MainScreen.route) {
                            MainScreen()
                        }

                    }
                }
            }
        }
    }

    sealed class Screen(val route: String) {
        object MainScreen : Screen("main_screen")

        fun withArgs(vararg args: String): String {
            return buildString {
                append(true)
                args.forEach { arg -> append("/$arg") }
            }
        }
    }
}

