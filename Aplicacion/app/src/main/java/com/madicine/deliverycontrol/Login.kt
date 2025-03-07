package com.madicine.deliverycontrol

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.madicine.deliverycontrol.Entities.Usuario
import com.madicine.deliverycontrol.viewModels.UsuariosViewModel

class Login : AppCompatActivity() {

    private lateinit var btnIniciarSesion: Button
    private lateinit var txt_email: EditText
    private lateinit var txt_pass: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var btn_register_redirect: Button
    private lateinit var btnForgotPassword: Button
    private var emailGlobal: String? = null

    // ViewModel
    private val viewModel: UsuariosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicialización de vistas
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)
        txt_email = findViewById(R.id.txt_email)
        txt_pass = findViewById(R.id.txt_pass)
        btn_register_redirect = findViewById(R.id.btn_register_redirect)
        btnForgotPassword = findViewById(R.id.btnForgotPassword)

        setUp()

        // Observador del ViewModel
        viewModel.usuario.observe(this, Observer { usuario ->
            usuario?.let {
                showHome(emailGlobal ?: "", it.nombre, it.apellido, it.udi)
            } ?: run {
                println("Error al obtener la respuesta")
            }
        })
    }

    override fun onResume() {
        super.onResume()
        txt_email.text = null
        txt_pass.text = null
    }

    private fun setUp() {
        btnIniciarSesion.setOnClickListener {
            if (txt_email.text.isNotEmpty() && txt_pass.text.isNotEmpty()) {
                val email = txt_email.text.toString()
                val pass = txt_pass.text.toString()

                // Mostrar el diálogo de carga
                val loadingDialog = AlertDialog.Builder(this)
                    .setView(layoutInflater.inflate(R.layout.dialog_loading, null))
                    .setCancelable(false)
                    .create()
                loadingDialog.show()

                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    loadingDialog.dismiss()

                    if (task.isSuccessful) {
                        val user = task.result?.user

                        if (user?.isEmailVerified == true) {
                            emailGlobal = user.email ?: ""
                            viewModel.buscarUsuario(user.uid ?: "")
                        } else {
                            showAlert(
                                "Verificación requerida",
                                "Debes verificar tu correo antes de iniciar sesión."
                            )
                            auth.signOut() // Cierra sesión para evitar acceso sin verificación
                        }
                    } else {
                        showAlert("Advertencia", "Usuario o contraseña incorrectos")
                    }
                }
            }
        }

        btn_register_redirect.setOnClickListener {
            val intent = Intent(this, registroUser::class.java)
            startActivity(intent)
        }

        // Funcionalidad para recuperar contraseña con AlertDialog
        btnForgotPassword.setOnClickListener {
            showPasswordResetDialog()
        }
    }

    private fun showPasswordResetDialog() {
        val input = EditText(this)
        input.hint = "Ingrese su correo"

        AlertDialog.Builder(this)
            .setTitle("Recuperar contraseña")
            .setMessage("Ingrese su correo para recibir un enlace de recuperación:")
            .setView(input)
            .setPositiveButton("Enviar") { _, _ ->
                val email = input.text.toString().trim()
                if (email.isNotEmpty()) {
                    sendPasswordResetEmail(email)
                } else {
                    showAlert("Advertencia", "Debe ingresar un correo válido.")
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                showAlert("Recuperación de contraseña", "Se ha enviado un correo para restablecer tu contraseña.")
            }
            .addOnFailureListener {
                showAlert("Error", "No se pudo enviar el correo de recuperación. Verifica que el correo esté registrado.")
            }
    }

    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Aceptar", null)
            .show()
    }

    private fun showHome(email: String, nombre: String?, apellido: String?, uid: String?) {
        val provider = ProviderType.BASIC
        val usuario = Usuario(uid, nombre ?: "", apellido ?: "", email, "")
        val menuIntent = Intent(this, MenuPrincipal::class.java).apply {
            putExtra("usuario", usuario)
            putExtra("provider", provider.name)
        }
        startActivity(menuIntent)
    }
}