package com.madicine.deliverycontrol.Services

import AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class FirebaseAuthService(private val auth: FirebaseAuth) : AuthRepository {

    override suspend fun signIn(email: String, password: String): FirebaseUser? {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user
    }

    override suspend fun signUp(email: String, password: String): FirebaseUser? {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user
    }

    override fun sendEmailVerification(user: FirebaseUser?, onSuccess: () -> Unit, onError: () -> Unit) {
        user?.sendEmailVerification()
            ?.addOnSuccessListener { onSuccess() }
            ?.addOnFailureListener { onError() }
    }

    override fun isEmailVerified(user: FirebaseUser?): Boolean {
        return user?.isEmailVerified ?: false
    }

    override fun getEmail(user: FirebaseUser?): String {
        return user?.email ?: ""
    }

    override fun getUid(user: FirebaseUser?): String {
        return user?.uid ?: ""
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun sendPasswordReset(email: String, onSuccess: () -> Unit, onError: () -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError() }
    }
}

