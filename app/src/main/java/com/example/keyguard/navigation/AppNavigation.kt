package com.example.keyguard.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.keyguard.ui.AgregarContrasenaPage
import com.example.keyguard.ui.ContrasenasGuardadasPage
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.keyguard.viewmodel.ContrasenaStoreViewModel
import com.example.keyguard.ui.HomeScreen
// import com.example.keyguard.data.AppDatabase // si usas Room
// import androidx.compose.runtime.remember
// import androidx.compose.runtime.LaunchedEffect

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // ⬇️ crea el VM compartido para todo el gráfico
    val storeVm: ContrasenaStoreViewModel = viewModel()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }

        composable("add_password") {
            AgregarContrasenaPage(navController) { nueva ->
                storeVm.add(nueva)           // guarda en memoria
            }
        }

        composable("saved_passwords") {
            ContrasenasGuardadasPage(
                navController = navController,
                items = storeVm.items        // ⬅️ aquí ya “existe” storeVm
            )
        }
    }
}