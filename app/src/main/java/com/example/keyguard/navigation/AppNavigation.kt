package com.example.keyguard.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.example.keyguard.ui.HomeScreen

@Composable
fun AppNavigation(

    modifier: Modifier = Modifier,
    navController: NavHostController
) {


    NavHost(

        navController = navController,
        startDestination = "HomePage",
        modifier = modifier
    ) {
        composable("HomePage") {

            HomeScreen(navController = navController)
        }
    }
}
