package com.madicine.deliverycontrol

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.madicine.deliverycontrol.Entities.Usuario
import com.madicine.deliverycontrol.Interfaces.PermissionHandler
import com.madicine.deliverycontrol.Utilities.PermissionsUtils
import java.text.SimpleDateFormat
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.Locale

class MenuPrincipal : AppCompatActivity() {

    private lateinit var permissionHandler: PermissionHandler
    private lateinit var tl_usuario: TextView
    private lateinit var tl_fecha: TextView
    private lateinit var tl_email2: TextView
    private lateinit var tl_provider: TextView
    private lateinit var tlCodigo: TextView
    private lateinit var btn_cerrar: Button
    private lateinit var btnCamara: Button
    private lateinit var barcodeScannerLauncher: ActivityResultLauncher<Intent>
    private var usuario: Usuario? = null

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
        tl_email2 = findViewById(R.id.tl_email2)
        tl_provider = findViewById(R.id.tl_provider)
        tlCodigo = findViewById(R.id.tlCodigo)
        btn_cerrar = findViewById(R.id.btn_cerrar)
        btnCamara = findViewById(R.id.btnCamara)

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = sdf.format(System.currentTimeMillis())
        tl_fecha.text = currentDate

        val bundle = intent.extras

        //Recuperar datos del usuario
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            usuario = bundle?.getSerializable("usuario", Usuario::class.java)
        }else{
            usuario = bundle?.getSerializable("usuario") as? Usuario
        }

        val nombre = usuario?.nombre + " " + usuario?.apellido
        val email = usuario?.email
        val provider = usuario?.udi

        //Datos de usuario
        tl_usuario.text = nombre
        tl_email2.text = email
        tl_provider.text = provider

        //Configuración
        setup()

        barcodeScannerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val scannedCode = result.data?.getStringExtra("scanned_code")
                tlCodigo.text = scannedCode ?: "No se pudo escanear el código"
            }
        }
    }

    private fun setup() {

        btn_cerrar.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

        btnCamara.setOnClickListener {
            if (permissionHandler.verifyCameraPermission(this)) {
                val intent = Intent(this, BarcodeScanner::class.java)
                barcodeScannerLauncher.launch(intent) // Usar barcodeScannerLauncher para iniciar la actividad
            }
        }
    }
}