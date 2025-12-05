package com.example.keyguard.data.bd.repo

import com.example.keyguard.api.RetrofitInstance
import com.example.keyguard.data.model.carpeta

class CarpetaRepo {

    suspend fun obtenerCarpetas(): List<carpeta>? {
        val response = RetrofitInstance.api.getCarpetas()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun agregarCarpeta(carp: carpeta): carpeta? {
        val response = RetrofitInstance.api.addCarpeta(carp)
        return if (response.isSuccessful) response.body() else null
    }
}