package com.madicine.deliverycontrol

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.madicine.deliverycontrol.Entities.Usuario
import com.madicine.deliverycontrol.Interfaces.PermissionHandler
import com.madicine.deliverycontrol.Utilities.PermissionsUtils
import com.madicine.deliverycontrol.viewModels.MedicamentosViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class MenuPrincipal : AppCompatActivity() {

    private lateinit var permissionHandler: PermissionHandler
    private lateinit var tl_usuario: TextView
    private lateinit var tl_fecha: TextView
    private lateinit var tlDatos: TextView
    private lateinit var btnConsulta: Button
    private lateinit var btnILogOut: ImageButton
    private lateinit var barcodeScannerLauncher: ActivityResultLauncher<Intent>
    private var usuario: Usuario? = null
    private var scannedCode : String? = null

    //Declaracion de variable ViewModel
    private val viewModel: MedicamentosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        permissionHandler = PermissionsUtils

        /**
         * Verificar si el permiso de la cámara está habilitado
         */
        if (!permissionHandler.verifyCameraPermission(this)) {
            permissionHandler.requestCameraPermission(this)
        }

        tl_usuario = findViewById(R.id.tlUsuario);
        tl_fecha = findViewById(R.id.tlFecha)
        tlDatos = findViewById(R.id.tlDatos)
        btnILogOut = findViewById(R.id.imgLogOut)
        btnConsulta = findViewById(R.id.btnConsulta)

        val bundle = intent.extras
        //Recuperar datos del usuario
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            usuario = bundle?.getSerializable("usuario", Usuario::class.java)
        }else{
            usuario = bundle?.getSerializable("usuario") as? Usuario
        }

        val nombre = usuario?.nombre + " " + usuario?.apellido
        val email = usuario?.email
        val uid = usuario?.udi
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = sdf.format(System.currentTimeMillis())

        /**
         * Proceso de guardado de datos en shared preferences
         * */
        val prefs = getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("uid",uid)
        prefs.putString("nombre",nombre)
        prefs.apply()


        //Datos de usuario
        tl_usuario.text = nombre
        tl_fecha.text = currentDate

        //Se obtienen los resultados del scanner
        barcodeScannerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                scannedCode = result.data?.getStringExtra("scanned_code")
                scannedCode?.let {
                    viewModel.enviarCodigoBarras(it,uid.toString())
                }
            }
        }

        //Lectura de ViewModel
        viewModel.respuesta.observe(this, Observer { respuesta ->
            respuesta?.let {
                tlDatos.text = it
            } ?: run {
                tlDatos.text = "Error al obtener la respuesta"
            }
        })

        //Configuración
        setup()
    }

    private fun setup() {

        btnConsulta.setOnClickListener {
            if (permissionHandler.verifyCameraPermission(this)) {
                val intent = Intent(this, BarcodeScanner::class.java)
                barcodeScannerLauncher.launch(intent) // Usar barcodeScannerLauncher para iniciar la actividad
            }
        }

        btnILogOut.setOnClickListener {
            logOut()
        }
    }

    /**
     * Metodo para cerrar sesión
     */
    private fun logOut(){
        val builder = AlertDialog.Builder(this)

        builder.setMessage(R.string.alertLogOut)

        // Configuración del botón "Sí"
        builder.setPositiveButton("Sí") { dialog, _ ->
            //Borrado de datos de SharedPreferenses
            val prefs = getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()

            //Cerrar sesión de firebase
            FirebaseAuth.getInstance().signOut()
            onBackPressed()

            dialog.dismiss()
        }

        // Configuración del botón "No"
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}