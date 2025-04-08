import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun signIn(email: String, password: String): FirebaseUser?
    suspend fun signUp(email: String, password: String): FirebaseUser?
    fun sendEmailVerification(user: FirebaseUser?, onSuccess: () -> Unit, onError: () -> Unit)
    fun isEmailVerified(user: FirebaseUser?): Boolean
    fun getEmail(user: FirebaseUser?): String
    fun getUid(user: FirebaseUser?): String
    fun signOut()
    fun sendPasswordReset(email: String, onSuccess: () -> Unit, onError: () -> Unit)
}
