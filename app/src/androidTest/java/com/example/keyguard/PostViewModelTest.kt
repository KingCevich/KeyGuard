package com.example.keyguard

import app.cash.turbine.test
import com.example.keyguard.model.Post
import com.example.keyguard.data.bd.repo.PostRepository
import com.example.keyguard.viewmodel.PostViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@OptIn(ExperimentalCoroutinesApi::class)
class PostViewModelTest {

    private lateinit var repository: PostRepository
    private lateinit var viewModel: PostViewModel

    @Before
    fun setup() {
        repository = mock(PostRepository::class.java)
        viewModel = PostViewModel()
    }

    @Test
    fun `loadPosts actualiza el flujo con los posts del repositorio`() = runTest {
        // Arrange
        val fakePosts = listOf(Post(1, 1, "Post 1", "Contenido 1"))
        `when`(repository.getPosts()).thenReturn(fakePosts)

        // Act
        viewModel.loadPosts()

        // Assert
        viewModel.posts.test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("Post 1", result[0].title)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
