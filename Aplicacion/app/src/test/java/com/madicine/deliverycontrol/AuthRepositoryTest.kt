package com.madicine.deliverycontrol

import com.google.firebase.auth.FirebaseUser
import com.madicine.deliverycontrol.Repositories.AuthRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class AuthRepositoryTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var mockUser: FirebaseUser

    @Before
    fun setUp() {
        authRepository = mock(AuthRepository::class.java)
        mockUser = mock(FirebaseUser::class.java)
    }

    @Test
    fun `signIn returns user when credentials are correct`(): Unit = runBlocking {
        // Arrange
        val email = "test@example.com"
        val password = "123456"
        `when`(authRepository.signIn(email, password)).thenReturn(mockUser)

        // Act
        val result = authRepository.signIn(email, password)

        // Assert
        assertNotNull(result)
        verify(authRepository).signIn(email, password)
    }

    @Test
    fun `isEmailVerified returns true`() {
        // Arrange
        `when`(authRepository.isEmailVerified(mockUser)).thenReturn(true)

        // Act
        val result = authRepository.isEmailVerified(mockUser)

        // Assert
        assertTrue(result)
    }
}
