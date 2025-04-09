package com.madicine.deliverycontrol

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.madicine.deliverycontrol.Services.FirebaseAuthService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class FirebaseAuthServiceTest {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authService: FirebaseAuthService

    @Before
    fun setUp() {
        firebaseAuth = mock()
        authService = FirebaseAuthService(firebaseAuth)
    }

    @Test
    fun `signIn returns FirebaseUser on success`() = runTest {
        val mockUser = mock<FirebaseUser>()
        val mockResult = mock<AuthResult> {
            on { user } doReturn mockUser
        }
        val task = mock<com.google.android.gms.tasks.Task<AuthResult>> {
            onBlocking { await() } doReturn mockResult
        }
        whenever(firebaseAuth.signInWithEmailAndPassword(any(), any())).thenReturn(task)

        val result = authService.signIn("test@mail.com", "123456")
        assertEquals(mockUser, result)
    }

    @Test
    fun `signUp returns FirebaseUser on success`() = runTest {
        val mockUser = mock<FirebaseUser>()
        val mockResult = mock<AuthResult> {
            on { user } doReturn mockUser
        }
        val task = mock<com.google.android.gms.tasks.Task<AuthResult>> {
            onBlocking { await() } doReturn mockResult
        }
        whenever(firebaseAuth.createUserWithEmailAndPassword(any(), any())).thenReturn(task)

        val result = authService.signUp("test@mail.com", "123456")
        assertEquals(mockUser, result)
    }

    @Test
    fun `isEmailVerified returns false when user is null`() {
        val result = authService.isEmailVerified(null)
        assertFalse(result)
    }

    @Test
    fun `getEmail returns empty string when user is null`() {
        val result = authService.getEmail(null)
        assertEquals("", result)
    }

    @Test
    fun `getUid returns empty string when user is null`() {
        val result = authService.getUid(null)
        assertEquals("", result)
    }
}
