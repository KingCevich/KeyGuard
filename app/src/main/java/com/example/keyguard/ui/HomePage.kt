package com.example.keyguard.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.keyguard.security.BioStatus
import com.example.keyguard.security.biometricsStatus
import com.example.keyguard.security.findActivity
import com.example.keyguard.security.launchEnroll
import com.example.keyguard.security.showBiometricPrompt
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = remember { context.findActivity() } // puede ser null brevemente
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var alreadyTried by remember { mutableStateOf(false) }

    // Dispara una vez cuando ya tenemos Activity disponible
    LaunchedEffect(activity) {
        if (!alreadyTried && activity != null) {
            alreadyTried = true
            when (biometricsStatus(activity)) {
                BioStatus.Success -> {
                    showBiometricPrompt(
                        activity = activity,
                        onSuccess = {
                            scope.launch { snackbarHostState.showSnackbar("Autenticado ✅") }
                            // navController.navigate("panel")
                        },
                        onFail = { msg ->
                            scope.launch { snackbarHostState.showSnackbar(msg) }
                        }
                    )
                }
                BioStatus.NoneEnrolled -> {
                    scope.launch {
                        snackbarHostState.showSnackbar("Configura huella o PIN en el sistema.")
                    }
                    launchEnroll(activity)
                }
                BioStatus.NoHardware -> {
                    scope.launch { snackbarHostState.showSnackbar("Sin hardware biométrico.") }
                    // aquí puedes navegar a un login de PIN propio
                }
                BioStatus.HWUnavailable -> {
                    scope.launch { snackbarHostState.showSnackbar("Hardware biométrico no disponible.") }
                }
                BioStatus.Unknown -> {
                    scope.launch { snackbarHostState.showSnackbar("Biometría no soportada.") }
                }
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { _ ->
        Column(Modifier.fillMaxSize()) {
            Text("KeyGuard", style = MaterialTheme.typography.headlineSmall)

            Button(onClick = {
                val act = activity ?: return@Button
                when (biometricsStatus(act)) {
                    BioStatus.Success -> {
                        showBiometricPrompt(
                            activity = act,
                            onSuccess = {
                                scope.launch { snackbarHostState.showSnackbar("Autenticado ✅") }
                            },
                            onFail = { msg -> scope.launch { snackbarHostState.showSnackbar(msg) } }
                        )
                    }
                    BioStatus.NoneEnrolled -> {
                        scope.launch { snackbarHostState.showSnackbar("Configura huella o PIN.") }
                        launchEnroll(act)
                    }
                    BioStatus.NoHardware -> {
                        scope.launch { snackbarHostState.showSnackbar("Sin hardware biométrico.") }
                    }
                    BioStatus.HWUnavailable -> {
                        scope.launch { snackbarHostState.showSnackbar("Hardware no disponible.") }
                    }
                    BioStatus.Unknown -> {
                        scope.launch { snackbarHostState.showSnackbar("No soportado.") }
                    }
                }
            }) {
                Text("Desbloquear con huella")
            }
        }
    }
}
