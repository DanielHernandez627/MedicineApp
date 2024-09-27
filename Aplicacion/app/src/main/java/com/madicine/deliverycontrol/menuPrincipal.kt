package com.madicine.deliverycontrol

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class menuPrincipal : AppCompatActivity() {

    private lateinit var tl_email2 : TextView
    private lateinit var tl_provider : TextView
    private lateinit var btn_cerrar : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tl_email2 = findViewById(R.id.tl_email2)
        tl_provider = findViewById(R.id.tl_provider)
        btn_cerrar = findViewById(R.id.btn_cerrar)

        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        setup(email ?: "",provider ?: "" )
    }

    private fun setup(email: String, provider: String){
        tl_email2.text = email
        tl_provider.text = provider

        btn_cerrar.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }
}