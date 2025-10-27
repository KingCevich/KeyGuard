package com.example.keyguard.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.keyguard.ui.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            // ⬇️ ¡OJO! HomeScreen YA NO recibe 'activity'
            HomeScreen(navController = navController)
        }

        // Ejemplo de otra ruta si luego la usas
        // composable("panel") { PanelScreen(navController) }
    }
}
