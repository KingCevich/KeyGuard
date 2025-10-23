package com.example.keyguard.data.model

data class contraseña(
    val id: Int,
    val contra: String,
    val carpetaId: Int?,
    val nombre: String,
    val key: Int,
    val descripcionpas: String? = null
)
