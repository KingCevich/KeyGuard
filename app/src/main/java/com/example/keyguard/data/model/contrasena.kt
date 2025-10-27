package com.example.keyguard.data.model

data class contrasena(
    val id: Int,
    val contra: String,
    val carpetaId: Int?,
    val nombre: String,
    val key: Int?,
    val descripcionpas: String? = null
)
