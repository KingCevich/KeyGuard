package com.example.keyguard.api
import com.example.keyguard.data.model.carpeta
import com.example.keyguard.data.model.contrasena
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("contrasenas")
    suspend fun getContrasenas(): Response<List<contrasena>>

    @GET("contrasenas/{id}")
    suspend fun getContrasena(@Path("id") id: Int): Response<contrasena>

    @POST("contrasenas")
    suspend fun addContrasena(@Body contrasena: contrasena): Response<contrasena>

    @PUT("contrasenas/{id}")
    suspend fun updateContrasena(
        @Path("id") id: Int,
        @Body contrasena: contrasena
    ): Response<contrasena>

    @DELETE("contrasenas/{id}")
    suspend fun deleteContrasena(@Path("id") id: Int): Response<Unit>



    @GET("carpetas")
    suspend fun getCarpetas(): Response<List<carpeta>>

    @GET("carpetas/{id}")
    suspend fun getCarpeta(@Path("id") id: Int): Response<carpeta>

    @POST("carpetas")
    suspend fun addCarpeta(@Body carpeta: carpeta): Response<carpeta>

    @PUT("carpetas/{id}")
    suspend fun updateCarpeta(
        @Path("id") id: Int,
        @Body carpeta: carpeta
    ): Response<carpeta>

    @DELETE("carpetas/{id}")
    suspend fun deleteCarpeta(@Path("id") id: Int): Response<Unit>
}