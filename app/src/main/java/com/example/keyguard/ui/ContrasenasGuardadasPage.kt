@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.keyguard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.keyguard.data.model.contrasena

@Composable
fun ContrasenasGuardadasPage(
    navController: NavController,
    items: List<contrasena>
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Contraseñas guardadas") }
            )
        }
    ) { inner ->
        if (items.isEmpty()) {
            // Estado vacío elegante
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner),
            ) {
                Text(
                    text = "Aún no hay contraseñas.\nToca “Agregar contraseña” para crear una.",
                    modifier = Modifier
                        .padding(24.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items) { item ->
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(item.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Text("Contraseña: ${item.contra}", style = MaterialTheme.typography.bodyMedium)
                            if (!item.descripcionpas.isNullOrBlank()) {
                                Text(item.descripcionpas!!, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}
