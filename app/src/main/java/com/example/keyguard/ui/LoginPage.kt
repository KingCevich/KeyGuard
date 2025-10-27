package com.example.keyguard.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.keyguard.R
import com.example.keyguard.security.BioStatus
import com.example.keyguard.security.biometricsStatus
import com.example.keyguard.security.findActivity
import com.example.keyguard.security.launchEnroll
import com.example.keyguard.security.showBiometricPrompt
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginPage(
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val activity = remember { context.findActivity() }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }


    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(1000)
            onLoginSuccess()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFF121212)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon),
                        contentDescription = "Logo de la aplicación",
                        modifier = Modifier.size(200.dp)
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = "KeyGuard",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(28.dp))

                    Button(
                        onClick = {
                            val act = activity ?: return@Button
                            if (biometricsStatus(act) == BioStatus.Success) {
                                showBiometricPrompt(
                                    activity = act,
                                    onSuccess = {
                                        isLoading = true
                                    },
                                    onFail = { msg ->
                                        scope.launch { snackbarHostState.showSnackbar(msg) }
                                        isLoading = false
                                    }
                                )
                            } else {
                                scope.launch { snackbarHostState.showSnackbar("Primero configura la biometría en tu dispositivo.") }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Desbloquear con huella")
                    }
                }
            }
        }
    }
}
