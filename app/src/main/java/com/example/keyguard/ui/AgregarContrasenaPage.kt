@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.keyguard.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.keyguard.data.model.contrasena
import com.example.keyguard.security.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AgregarContrasenaPage(
    navController: NavController,
    onConfirmSave: (contrasena) -> Unit
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    var nombre by remember { mutableStateOf("") }
    var contra by remember { mutableStateOf("") }
    var descripcionpas by remember { mutableStateOf("") }

    var showPinDialog by remember { mutableStateOf(false) }
    var pin by remember { mutableStateOf("") }
    var creatingPin by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }


    val textFieldColors = TextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.Gray,
        cursorColor = Color.White,
        focusedIndicatorColor = Color.White,
        unfocusedIndicatorColor = Color.Gray,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent
    )

    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(800)
            val c = contrasena(
                id = 0,
                contra = contra,
                carpetaId = null,
                nombre = nombre.trim(),
                key = null,
                descripcionpas = descripcionpas.trim().ifBlank { null }
            )
            onConfirmSave(c)
            Toast.makeText(ctx, "Guardado", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Crear contraseña") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1C1C1C),
                    titleContentColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbar) },
        containerColor = Color(0xFF121212),
        bottomBar = {
            Surface(tonalElevation = 3.dp, color = Color(0xFF1C1C1C)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = {
                            if (nombre.isBlank() || contra.isBlank()) {
                                scope.launch { snackbar.showSnackbar("Completa nombre y contraseña") }
                                return@Button
                            }


                            val act = ctx.findActivity()
                            if (act != null && canUseBiometrics(act)) {
                                showBiometricPrompt(
                                    activity = act,
                                    onSuccess = {
                                        isLoading = true
                                    },
                                    onFail = {
                                        creatingPin = !PinManager.hasPin(ctx)
                                        showPinDialog = true
                                        isLoading = false
                                    }
                                )
                            } else {
                                creatingPin = !PinManager.hasPin(ctx)
                                showPinDialog = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 14.dp),
                        enabled = !isLoading
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    ) { inner ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )

                    OutlinedTextField(
                        value = contra,
                        onValueChange = { contra = it },
                        label = { Text("Contraseña") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )

                    OutlinedTextField(
                        value = descripcionpas,
                        onValueChange = { descripcionpas = it },
                        label = { Text("Descripción (opcional)") },
                        minLines = 3,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp),
                        colors = textFieldColors
                    )
                    Spacer(Modifier.height(90.dp))
                }
            }
        }
    }

    if (showPinDialog) {}
}
