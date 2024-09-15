package com.madicine.deliverycontrol

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

enum class ProviderType {
    BASIC
}


class MainActivity : AppCompatActivity() {

    private lateinit var btnPrincipal: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnPrincipal = findViewById(R.id.btnPrincipal)

        btnPrincipal.setOnClickListener {
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
        }

        //checkFirebaseAuthentication()
    }

    /*private fun checkFirebaseAuthentication() {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión anónimo exitoso
                    val user: FirebaseUser? = auth.currentUser
                    Toast.makeText(this,"Inicio de sesión exitoso, usuario: ${user?.uid}",Toast.LENGTH_LONG).show()
                } else {
                    // Fallo en la conexión
                    Toast.makeText(this,"Fallo en la conexión a Firebase Authentication",Toast.LENGTH_LONG).show()
                }
            }
    }*/
}