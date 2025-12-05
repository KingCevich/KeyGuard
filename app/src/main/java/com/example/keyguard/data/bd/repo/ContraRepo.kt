package com.example.keyguard.data.bd.repo
import com.example.keyguard.api.RetrofitInstance
import com.example.keyguard.data.model.contrasena

class ContraRepo {

    suspend fun obtenerContrasenas(): List<contrasena>? {
        val response = RetrofitInstance.api.getContrasenas()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun agregarContrasena(contra: contrasena): contrasena? {
        val response = RetrofitInstance.api.addContrasena(contra)
        return if (response.isSuccessful) response.body() else null
    }
}