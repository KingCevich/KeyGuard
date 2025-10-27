@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.keyguard.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.keyguard.security.*
import com.example.keyguard.data.model.contrasena
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

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Crear contraseña") })
        },
        snackbarHost = { SnackbarHost(snackbar) },
        bottomBar = {
            // Botón fijo inferior, con padding seguro
            Surface(tonalElevation = 3.dp) {
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
                                        val c = contrasena(
                                            id = 0,
                                            contra = contra,
                                            carpetaId = null,
                                            nombre = nombre.trim(),
                                            key = null,
                                            descripcionpas = descripcionpas.trim().ifBlank { null }
                                        )
                                        onConfirmSave(c)
                                        Toast.makeText(ctx, "Guardado ✅", Toast.LENGTH_SHORT).show()
                                        navController.popBackStack()
                                    },
                                    onFail = {
                                        creatingPin = !PinManager.hasPin(ctx)
                                        showPinDialog = true
                                    }
                                )
                            } else {
                                creatingPin = !PinManager.hasPin(ctx)
                                showPinDialog = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 14.dp)
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    ) { inner ->
        // Contenido scrollable con buen padding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
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
                modifier = Modifier.fillMaxWidth()
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
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcionpas,
                onValueChange = { descripcionpas = it },
                label = { Text("Descripción (opcional)") },
                minLines = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp)
            )

            // Espacio para que el contenido no quede oculto detrás del botón inferior
            Spacer(Modifier.height(90.dp))
        }
    }

    // Diálogo de PIN
    if (showPinDialog) {
        AlertDialog(
            onDismissRequest = { showPinDialog = false; pin = "" },
            title = { Text(if (creatingPin) "Configurar PIN" else "Confirmar con PIN") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(if (creatingPin) "Crea un PIN de 4–6 dígitos" else "Ingresa tu PIN")
                    OutlinedTextField(
                        value = pin,
                        onValueChange = { if (it.length <= 6) pin = it.filter(Char::isDigit) },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        label = { Text("PIN") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (pin.length < 4) {
                        scope.launch { snackbar.showSnackbar("El PIN debe tener al menos 4 dígitos") }
                        return@TextButton
                    }
                    if (creatingPin) {
                        PinManager.setPin(ctx, pin)
                        Toast.makeText(ctx, "PIN configurado", Toast.LENGTH_SHORT).show()
                        showPinDialog = false
                        pin = ""
                    } else {
                        val ok = PinManager.verify(ctx, pin)
                        if (ok) {
                            val c = contrasena(
                                id = 0,
                                contra = contra,
                                carpetaId = null,
                                nombre = nombre.trim(),
                                key = null,
                                descripcionpas = descripcionpas.trim().ifBlank { null }
                            )
                            onConfirmSave(c)
                            showPinDialog = false
                            pin = ""
                            Toast.makeText(ctx, "Guardado ✅", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        } else {
                            scope.launch { snackbar.showSnackbar("PIN incorrecto") }
                        }
                    }
                }) { Text(if (creatingPin) "Guardar PIN" else "Confirmar") }
            },
            dismissButton = {
                TextButton(onClick = { showPinDialog = false; pin = "" }) { Text("Cancelar") }
            }
        )
    }
}
