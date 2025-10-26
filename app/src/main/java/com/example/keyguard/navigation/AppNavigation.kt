package com.example.keyguard.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity // <-- PASO 1: AÑADE ESTE IMPORT
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.keyguard.ui.HomeScreen // Asegúrate de que este import sea el de tu pantalla

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    activity: FragmentActivity
) {
    NavHost(
        navController = navController,
        startDestination = "home_screen" // O como se llame tu ruta
    ) {
        composable("home_screen") {

            HomeScreen(
                navController = navController,
                activity = activity
            )
        }
    }
}
