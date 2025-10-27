package com.example.keyguard.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.keyguard.ui.AgregarContrasenaPage
import com.example.keyguard.ui.HomeScreen
// import com.example.keyguard.data.AppDatabase // si usas Room
// import androidx.compose.runtime.remember
// import androidx.compose.runtime.LaunchedEffect

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }

        composable("add_password") {
            AgregarContrasenaPage(navController) { nueva ->
                // Ejemplo si tu Entity es la misma clase:
                // val ctx = LocalContext.current
                // val db = remember { AppDatabase.get(ctx) }
                // LaunchedEffect(nueva) { db.contrasenaDao().insert(nueva) }
            }
        }
    }
}