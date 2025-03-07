package com.madicine.deliverycontrol

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.madicine.deliverycontrol.viewModels.UsuariosViewModel

class registroUser : AppCompatActivity() {

    private lateinit var scrollView: ScrollView
    private lateinit var etEmail: EditText
    private lateinit var etConfirmEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var tvEmailWarning: TextView
    private lateinit var tvEmailFormatWarning: TextView
    private lateinit var tvPasswordWarning: TextView
    private lateinit var tvPasswordLengthWarning: TextView
    private lateinit var btnRegister: Button
    private lateinit var auth: FirebaseAuth

    private val viewModel: UsuariosViewModel by viewModels()

    private val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth
        setContentView(R.layout.activity_registro_user)

        initViews()
        setUp()
    }

    private fun initViews() {
        scrollView = findViewById(R.id.scrollView)
        etEmail = findViewById(R.id.et_email)
        etConfirmEmail = findViewById(R.id.et_confirm_email)
        etPassword = findViewById(R.id.et_password)
        etConfirmPassword = findViewById(R.id.et_confirm_password)
        etNombre = findViewById(R.id.et_nombre)
        etApellido = findViewById(R.id.et_apellido)
        tvEmailWarning = findViewById(R.id.tv_email_warning)
        tvEmailFormatWarning = findViewById(R.id.tv_email_format_warning)
        tvPasswordWarning = findViewById(R.id.tv_password_warning)
        tvPasswordLengthWarning = findViewById(R.id.tv_password_length_warning)
        btnRegister = findViewById(R.id.btn_register)
    }

    private fun setUp() {
        btnRegister.setOnClickListener {
            if (validateInput()) {
                showTermsDialog()
            }
        }
    }

    private fun showTermsDialog() {
        val checkBox = CheckBox(this).apply {
            text = "Acepto los términos y condiciones"
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Términos y Condiciones")
        builder.setMessage("Aquí iría la descripción de los términos y condiciones...")
        builder.setView(checkBox)
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            if (checkBox.isChecked) {
                registerUser()
            } else {
                Toast.makeText(this, "Debes aceptar los términos y condiciones", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun registerUser() {
        val email = etConfirmEmail.text.toString()
        val password = etConfirmPassword.text.toString()
        val nombre = etNombre.text.toString()
        val apellido = etApellido.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val uid = user?.uid ?: ""

                    // Guardar usuario en la base de datos de la API inmediatamente
                    viewModel.crearUsuario(nombre, apellido, uid)

                    // Enviar correo de verificación
                    user?.sendEmailVerification()?.addOnCompleteListener { emailTask ->
                        if (emailTask.isSuccessful) {
                            showAlertAndRedirect()
                        } else {
                            showAlert("Error", "No se pudo enviar el correo de verificación.")
                        }
                    }
                } else {
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        showAlert("Error", "El correo ya está en uso.")
                    } else {
                        showAlert("Error", "Se produjo un error al registrar el usuario.")
                    }
                }
            }
    }

    private fun showAlertAndRedirect() {
        AlertDialog.Builder(this)
            .setTitle("Registro exitoso")
            .setMessage("Se ha enviado un correo de verificación. Por favor, verifica tu correo antes de iniciar sesión.")
            .setPositiveButton("Aceptar") { _, _ ->
                finish()
            }
            .show()
    }
    private fun validateInput(): Boolean {
        var isValid = true

        if (!TextUtils.equals(etEmail.text.toString(), etConfirmEmail.text.toString())) {
            tvEmailWarning.visibility = View.VISIBLE
            isValid = false
        } else {
            tvEmailWarning.visibility = View.GONE
        }

        if (!emailPattern.matches(etEmail.text.toString())) {
            tvEmailFormatWarning.visibility = View.VISIBLE
            isValid = false
        } else {
            tvEmailFormatWarning.visibility = View.GONE
        }

        if (!TextUtils.equals(etPassword.text.toString(), etConfirmPassword.text.toString())) {
            tvPasswordWarning.visibility = View.VISIBLE
            isValid = false
        } else {
            tvPasswordWarning.visibility = View.GONE
        }

        if (etPassword.text.toString().length < 6) {
            tvPasswordLengthWarning.visibility = View.VISIBLE
            isValid = false
        } else {
            tvPasswordLengthWarning.visibility = View.GONE
        }

        return isValid
    }

    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Aceptar", null)
            .show()
    }
}
