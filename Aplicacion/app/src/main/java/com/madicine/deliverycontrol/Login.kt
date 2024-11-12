package com.madicine.deliverycontrol

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.madicine.deliverycontrol.Entities.Usuario
import com.madicine.deliverycontrol.viewModels.UsuariosViewModel

class Login : AppCompatActivity() {

    private lateinit var btnIniciarSesion: Button
    private lateinit var txt_email: EditText
    private lateinit var txt_pass: EditText
    private lateinit var auth: FirebaseAuth;
    private lateinit var btn_register_redirect: Button
    private lateinit var imgBGoogle: Button
    private val GOOGLE_SIGN_IN = 100
    private var emailGlobal : String? = null

    //Declaracion de variable ViewModel
    private val viewModel: UsuariosViewModel by viewModels()

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

        //Inicializacion de objetos graficos
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)
        txt_email = findViewById(R.id.txt_email)
        txt_pass = findViewById(R.id.txt_pass)
        btn_register_redirect = findViewById(R.id.btn_register_redirect)
        imgBGoogle = findViewById(R.id.imgBGoogle)

        //Configuracion de inicio
        setUp()

        //Lectura de ViewModel
        viewModel.usuario.observe(this, Observer { usuario ->
            usuario?.let {
                usuario.let {
                    showHome(emailGlobal ?: "",it.nombre,it.apellido,it.udi)
                }
            } ?: run {
                println("Error al obtener la respuesta")
            }
        })
    }

    private fun setUp(){
        btnIniciarSesion.setOnClickListener{
            if (txt_email.text.isNotEmpty() && txt_pass.text.isNotEmpty()){
                val email = txt_email.text.toString()
                val pass = txt_pass.text.toString()
                auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener{
                    if(it.isSuccessful){
                        emailGlobal = it.result?.user?.email ?: ""
                        viewModel.buscarUsuario(it.result?.user?.uid ?: "")
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

        /*imgBGoogle.setOnClickListener{
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this,googleConf)

            startActivityForResult(googleClient.signInIntent,GOOGLE_SIGN_IN)
        }*/
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Advertencia")
        builder.setMessage("Usuario o contraseña incorrectos")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email:String,nombre: String?, apellido: String?,uid: String?){
        val provider = ProviderType.BASIC
        val usuario = Usuario(uid , nombre ?: "",apellido ?: "",email,"");
        val menuIntent = Intent(this,MenuPrincipal::class.java).apply {
            putExtra("usuario",usuario)
            putExtra("provider",provider.name)
        }
        startActivity(menuIntent)
    }
}