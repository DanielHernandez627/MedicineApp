package com.madicine.deliverycontrol

import AuthRepository
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.madicine.deliverycontrol.Services.FirebaseAuthService
import com.madicine.deliverycontrol.viewModels.UsuariosViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    private lateinit var authService: AuthRepository

    private val viewModel: UsuariosViewModel by viewModels()

    private val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        authService = FirebaseAuthService(auth)

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
        val scrollView = ScrollView(this)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 20, 40, 20)
        }

        val textView = TextView(this).apply {
            text = getString(R.string.terminos_y_condiciones)
            textSize = 14f
            setPadding(20, 20, 20, 20)
        }

        val checkBox = CheckBox(this).apply {
            text = "Acepto los términos y condiciones"
        }

        layout.addView(textView)
        layout.addView(checkBox)
        scrollView.addView(layout)

        AlertDialog.Builder(this)
            .setTitle("Términos y Condiciones")
            .setView(scrollView)
            .setPositiveButton("Aceptar") { dialog, _ ->
                if (checkBox.isChecked) {
                    registerUser()
                } else {
                    Toast.makeText(this, "Debes aceptar los términos y condiciones", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun registerUser() {
        val email = etConfirmEmail.text.toString()
        val password = etConfirmPassword.text.toString()
        val nombre = etNombre.text.toString()
        val apellido = etApellido.text.toString()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val user = authService.signUp(email, password)
                val uid = authService.getUid(user)

                viewModel.crearUsuario(nombre, apellido, uid)

                authService.sendEmailVerification(user,
                    onSuccess = { showAlertAndRedirect() },
                    onError = { showAlert("Error", "No se pudo enviar el correo de verificación.") }
                )

            } catch (e: Exception) {
                if (e.message?.contains("email address is already in use") == true) {
                    showAlert("Error", "El correo ya está en uso.")
                } else {
                    showAlert("Error", "Se produjo un error al registrar el usuario.")
                }
            }
        }
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

    private fun showAlertAndRedirect() {
        AlertDialog.Builder(this)
            .setTitle("Registro exitoso")
            .setMessage("Se ha enviado un correo de verificación. Por favor, verifica tu correo antes de iniciar sesión.")
            .setPositiveButton("Aceptar") { _, _ ->
                finish()
            }
            .show()
    }
}
