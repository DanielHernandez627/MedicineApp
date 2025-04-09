package com.madicine.deliverycontrol

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
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
import com.madicine.deliverycontrol.Entities.Medicamentos
import com.madicine.deliverycontrol.Entities.Usuario
import com.madicine.deliverycontrol.Interfaces.PermissionHandler
import com.madicine.deliverycontrol.Services.SessionService
import com.madicine.deliverycontrol.Services.SharedPrefsService
import com.madicine.deliverycontrol.Utilities.PermissionsUtils
import com.madicine.deliverycontrol.viewModels.MedicamentosViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class MenuPrincipal : AppCompatActivity() {

    private lateinit var permissionHandler: PermissionHandler
    private lateinit var sessionService: SessionService

    private lateinit var tl_usuario: TextView
    private lateinit var tl_fecha: TextView
    private lateinit var btnConsulta: Button
    private lateinit var btnILogOut: ImageButton
    private lateinit var btnMedicamentos: Button
    private lateinit var btnHistorial: Button
    private lateinit var barcodeScannerLauncher: ActivityResultLauncher<Intent>

    private var usuario: Usuario? = null
    private var scannedCode: String? = null
    private var uid: String? = null

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
        sessionService = SessionService(SharedPrefsService(this))

        if (!permissionHandler.verifyCameraPermission(this)) {
            permissionHandler.requestCameraPermission(this)
        }

        tl_usuario = findViewById(R.id.tlUsuario)
        tl_fecha = findViewById(R.id.tlFecha)
        btnILogOut = findViewById(R.id.imgLogOut)
        btnConsulta = findViewById(R.id.btnConsulta)
        btnMedicamentos = findViewById(R.id.btnMedicamentos)
        btnHistorial = findViewById(R.id.btnHistorial)

        val bundle = intent.extras
        usuario = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle?.getSerializable("usuario", Usuario::class.java)
        } else {
            bundle?.getSerializable("usuario") as? Usuario
        }

        val nombre = "${usuario?.nombre} ${usuario?.apellido}"
        val email = usuario?.email ?: ""
        uid = usuario?.udi

        // Guardar datos de sesión
        sessionService.saveSessionData(nombre, email, uid ?: "")

        // Mostrar en pantalla
        tl_usuario.text = nombre
        tl_fecha.text = getCurrentDate()

        barcodeScannerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                scannedCode = result.data?.getStringExtra("scanned_code")
                scannedCode?.let {
                    viewModel.enviarCodigoBarras(it, uid.toString())
                }
            }
        }

        viewModel.medicamento.observe(this, Observer { respuesta ->
            respuesta?.let {
                val medicamento = Medicamentos(
                    it.nombre, it.concentracion, it.codigo,
                    it.idMedicamento, it.idLaboratorio,
                    it.tipoMedicamento, it.contraIndicacion, it.nombreLaboratorio
                )
                showMedicine(medicamento)
            } ?: run {
                showAlertOk(this)
                Log.d("MenuPrincipal", "Error al obtener la respuesta")
            }
        })

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                logOut()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        setup()
    }

    private fun setup() {
        btnConsulta.setOnClickListener {
            if (permissionHandler.verifyCameraPermission(this)) {
                val intent = Intent(this, BarcodeScanner::class.java)
                barcodeScannerLauncher.launch(intent)
            }
        }

        btnILogOut.setOnClickListener {
            logOut()
        }

        btnMedicamentos.setOnClickListener {
            val intent = Intent(this, ReportError::class.java).apply {
                putExtra("uuid", uid)
            }
            startActivity(intent)
        }

        btnHistorial.setOnClickListener {
            val intent = Intent(this, Historic::class.java).apply {
                putExtra("uuid", uid)
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        tl_usuario.text = sessionService.getNombre()
        tl_fecha.text = getCurrentDate()
        uid = sessionService.getUid()
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(System.currentTimeMillis())
    }

    private fun logOut() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.alertLogOut)
        builder.setPositiveButton("Sí") { dialog, _ ->
            sessionService.clearSession()
            FirebaseAuth.getInstance().signOut()
            finish()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun showMedicine(medicamento: Medicamentos) {
        val intent = Intent(this, ViewMedicine::class.java).apply {
            putExtra("Medicamento", medicamento)
        }
        startActivity(intent)
    }

    private fun showAlertOk(context: Context) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Error")
            .setMessage("Código de barras o QR no reconocido")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }
}
