package com.example.keyguard.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.keyguard.ui.AgregarContrasenaPage
import com.example.keyguard.ui.ContrasenasGuardadasPage
import com.example.keyguard.ui.HomeScreen
import com.example.keyguard.ui.LoginPage // 1. Asegúrate de importar LoginPage
import com.example.keyguard.viewmodel.ContrasenaStoreViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val storeVm: ContrasenaStoreViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {


        composable("login") {
            LoginPage(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                }
            )
        }


        composable("home") {
            HomeScreen(navController)
        }


        composable("add_password") {
            AgregarContrasenaPage(navController) { nueva ->
                storeVm.add(nueva)
            }
        }

        composable("saved_passwords") {
            ContrasenasGuardadasPage(
                navController = navController,
                items = storeVm.items
            )
        }
    }
}
