@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.keyguard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
    val activity = remember { context.findActivity() }          // puede ser null brevemente
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
                            scope.launch { snackbarHostState.showSnackbar("Autenticado") }
                            // navController.navigate("panel")
                        },
                        onFail = { msg -> scope.launch { snackbarHostState.showSnackbar(msg) } }
                    )
                }
                BioStatus.NoneEnrolled -> {
                    scope.launch { snackbarHostState.showSnackbar("Configura huella o PIN en el sistema.") }
                    launchEnroll(activity)
                }
                BioStatus.NoHardware -> {
                    scope.launch { snackbarHostState.showSnackbar("Sin hardware biométrico.") }
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

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("KeyGuard") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { inner ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Botón: Agregar contraseña
                Button(
                    onClick = { navController.navigate("add_password") },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 14.dp)
                ) {
                    Text("Agregar contraseña")
                }

                // Botón: Desbloquear con huella
                OutlinedButton(
                    onClick = {
                        val act = activity ?: return@OutlinedButton
                        when (biometricsStatus(act)) {
                            BioStatus.Success -> {
                                showBiometricPrompt(
                                    activity = act,
                                    onSuccess = {
                                        scope.launch { snackbarHostState.showSnackbar("Autenticado") }
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
                    },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 14.dp)
                ) {
                    Text("Desbloquear con huella")
                }
            }
        }
    }
}
