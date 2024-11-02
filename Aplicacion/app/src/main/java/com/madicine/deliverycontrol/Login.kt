package com.madicine.deliverycontrol

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.madicine.deliverycontrol.Entities.Usuario

class Login : AppCompatActivity() {

    private lateinit var btnIniciarSesion: Button
    private lateinit var txt_email: EditText
    private lateinit var txt_pass: EditText
    private lateinit var auth: FirebaseAuth;
    private lateinit var btn_register_redirect: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)
        txt_email = findViewById(R.id.txt_email)
        txt_pass = findViewById(R.id.txt_pass)
        btn_register_redirect = findViewById(R.id.btn_register_redirect)

        setUp()
    }

    private fun setUp(){
        btnIniciarSesion.setOnClickListener{
            if (txt_email.text.isNotEmpty() && txt_pass.text.isNotEmpty()){
                val email = txt_email.text.toString()
                val pass = txt_pass.text.toString()
                auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener{
                    if(it.isSuccessful){
                        showHome(it.result?.user?.email ?: "","Hola","Mundo",it.result?.user?.uid ?: "",ProviderType.BASIC)
                    }else{
                        showAlert()
                    }
                }
            }
        }

        btn_register_redirect.setOnClickListener{
            val intent = Intent(this,registroUser::class.java)
            startActivity(intent)
        }
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Advertencia")
        builder.setMessage("Usuario o contraseña incorrectos")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email:String,nombre: String, apellido: String,uid: String?, provider: ProviderType){
        val usuario = Usuario(uid ,nombre,apellido,email,"");
        val menuIntent = Intent(this,MenuPrincipal::class.java).apply {
            putExtra("usuario",usuario)
            putExtra("provider",provider.name)
        }
        startActivity(menuIntent)
    }
}