package com.example.keyguard.data.bd.repo

import com.example.keyguard.model.Post
import com.example.keyguard.data.remote.RetrofitInstance

class PostRepository {
    suspend fun getPosts(): List<Post>? {
        val response = RetrofitInstance.api.getPosts()
        return if (response.isSuccessful) response.body() else null
    }
}