package com.example.keyguard

import com.example.keyguard.model.Post
import com.example.keyguard.data.remote.ApiService
import com.example.keyguard.data.bd.repo.PostRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.Response

class PostRepositoryTest {

    private lateinit var repository: PostRepository
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        apiService = mock(ApiService::class.java)
        repository = PostRepository()
    }

    @Test
    fun `getPosts devuelve lista cuando API responde correctamente`() = runBlocking {
        // Arrange
        val fakePosts = listOf(
            Post(1, 1, "Título 1", "Cuerpo 1"),
            Post(1, 2, "Título 2", "Cuerpo 2")
        )

        val response = Response.success(fakePosts)
        `when`(apiService.getPosts()).thenReturn(response)

        // Act
        val result = repository.getPosts()

        // Assert
        assertNotNull(result)
        assertEquals(2, result?.size)
        assertEquals("Título 1", result?.get(0)?.title)
    }

    @Test
    fun `getPosts devuelve null cuando API responde error`() = runBlocking {
        // Arrange
        val response = Response.error<List<Post>>(404, okhttp3.ResponseBody.create(null, ""))
        `when`(apiService.getPosts()).thenReturn(response)

        // Act
        val result = repository.getPosts()

        // Assert
        assertNull(result)
    }
}
