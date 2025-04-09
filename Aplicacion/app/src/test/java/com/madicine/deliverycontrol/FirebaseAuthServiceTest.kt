package com.madicine.deliverycontrol

import android.app.Activity
import com.google.android.gms.tasks.*
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.madicine.deliverycontrol.Services.FirebaseAuthService
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

        val task = SuccessfulAuthTask(mockResult)
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

        val task = SuccessfulAuthTask(mockResult)
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

    private class SuccessfulAuthTask(private val result: AuthResult) : Task<AuthResult>() {
        override fun isComplete() = true
        override fun isSuccessful() = true
        override fun isCanceled() = false
        override fun getResult(): AuthResult = result
        override fun <X : Throwable?> getResult(exceptionType: Class<X>): AuthResult = result
        override fun getException(): Exception? = null

        override fun addOnSuccessListener(listener: OnSuccessListener<in AuthResult>): Task<AuthResult> {
            listener.onSuccess(result)
            return this
        }

        override fun addOnSuccessListener(activity: Activity, listener: OnSuccessListener<in AuthResult>): Task<AuthResult> {
            listener.onSuccess(result)
            return this
        }

        override fun addOnSuccessListener(executor: java.util.concurrent.Executor, listener: OnSuccessListener<in AuthResult>): Task<AuthResult> {
            listener.onSuccess(result)
            return this
        }

        override fun addOnFailureListener(listener: OnFailureListener): Task<AuthResult> = this
        override fun addOnFailureListener(activity: Activity, listener: OnFailureListener): Task<AuthResult> = this
        override fun addOnFailureListener(executor: java.util.concurrent.Executor, listener: OnFailureListener): Task<AuthResult> = this

        override fun addOnCompleteListener(listener: OnCompleteListener<AuthResult>): Task<AuthResult> {
            listener.onComplete(this)
            return this
        }

        override fun addOnCompleteListener(activity: Activity, listener: OnCompleteListener<AuthResult>): Task<AuthResult> {
            listener.onComplete(this)
            return this
        }

        override fun addOnCompleteListener(executor: java.util.concurrent.Executor, listener: OnCompleteListener<AuthResult>): Task<AuthResult> {
            listener.onComplete(this)
            return this
        }
    }
}
