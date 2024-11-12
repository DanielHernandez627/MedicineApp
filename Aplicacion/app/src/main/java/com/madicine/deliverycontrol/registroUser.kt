package com.madicine.deliverycontrol

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth
import com.madicine.deliverycontrol.Entities.Usuario
import com.madicine.deliverycontrol.viewModels.UsuariosViewModel

class registroUser : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etConfirmEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var tvEmailWarning: TextView
    private lateinit var tvPasswordWarning: TextView
    private lateinit var btnRegister: Button
    private lateinit var auth: FirebaseAuth;
    private var email: String? = null

    //Declaracion de variable ViewModel
    private val viewModel: UsuariosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etEmail = findViewById(R.id.et_email)
        etConfirmEmail = findViewById(R.id.et_confirm_email)
        etPassword = findViewById(R.id.et_password)
        etConfirmPassword = findViewById(R.id.et_confirm_password)
        etNombre = findViewById(R.id.et_nombre)
        etApellido = findViewById(R.id.et_apellido)
        tvEmailWarning = findViewById(R.id.tv_email_warning)
        tvPasswordWarning = findViewById(R.id.tv_password_warning)
        btnRegister = findViewById(R.id.btn_register)

        //Configuracion de SetUP
        setUp();
    }

    private fun setUp(){
        btnRegister.setOnClickListener{
            if (validateInput()){
                auth.createUserWithEmailAndPassword(etConfirmEmail.text.toString(),etConfirmPassword.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            email = it.result?.user?.email ?: ""
                            showHome(email,etNombre.text.toString(),etApellido.text.toString(),ProviderType.BASIC)
                        }else{
                            if (it.exception is FirebaseAuthUserCollisionException){
                                showAlertEmail()
                            }else{
                                showAlert()
                            }
                        }
                    }
            }
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true

        // Comprobar correos
        if (!TextUtils.equals(etEmail.text.toString(), etConfirmEmail.text.toString())) {
            tvEmailWarning.visibility = View.VISIBLE
            isValid = false
        } else {
            tvEmailWarning.visibility = View.GONE
        }
        // Comprobar contraseñas
        if (!TextUtils.equals(etPassword.text.toString(), etConfirmPassword.text.toString())) {
            tvPasswordWarning.visibility = View.VISIBLE
            isValid = false
        } else {
            tvPasswordWarning.visibility = View.GONE
        }

        return isValid
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error creando al usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email:String?,nombre: String, apellido: String, provider: ProviderType){
        val uid = auth.currentUser?.uid
        val usuario = Usuario(uid ,nombre,apellido,email,"");

        viewModel.crearUsuario(nombre,apellido,auth.currentUser?.uid.toString())

        val menuIntent = Intent(this,MenuPrincipal::class.java).apply {
            putExtra("usuario",usuario)
            putExtra("provider",provider.name)
        }
        menuIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(menuIntent)
        finish()
    }

    private fun showAlertEmail(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("El correo digitado ya se encuentra en uso")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}