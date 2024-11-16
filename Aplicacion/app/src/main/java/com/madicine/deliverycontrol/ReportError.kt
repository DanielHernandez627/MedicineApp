package com.madicine.deliverycontrol

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.madicine.deliverycontrol.Entities.ReporteMedicamentos
import com.madicine.deliverycontrol.viewModels.ReporteViewModel
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

class ReportError : AppCompatActivity() {

    private lateinit var btnFoto: Button
    private lateinit var imgPreview: ImageView
    private lateinit var edtDescription: EditText
    private lateinit var btnEnviar: Button
    private var imageBitmap: Bitmap? = null
    private lateinit var uuid : String

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    //Declaracion de variable ViewModel
    private val viewModel: ReporteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_report_error)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnFoto = findViewById(R.id.btnFoto)
        btnEnviar = findViewById(R.id.btnEnviarReporte)
        imgPreview = findViewById(R.id.imgPreview)
        edtDescription = findViewById(R.id.edtDescription)

        uuid = intent.getStringExtra("uuid").toString()

        setUp()


        //Lectura del ViewModel
        viewModel.isReportCreated.observe(this, Observer { isSuccess ->
            isSuccess?.let {
                isSuccess.let {
                    if (isSuccess) {
                        showAlertOk(this,"Reporte creado","El reporte ha sido creado correctamente")
                    } else {
                        Toast.makeText(this, "Error al crear el reporte", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }


    private fun setUp(){
        btnFoto.setOnClickListener {
            openCamera()
        }

        btnEnviar.setOnClickListener {
            sendReport()
        }
    }

    private fun openCamera() {
        takePicture.launch(null)
    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            imageBitmap = it

            //Guardar imagen en el dispositivo
            saveImageToStorage(it)

            imgPreview.setImageBitmap(it)
        }
    }

    private fun saveImageToStorage(bitmap: Bitmap): String? {
        val filename = "IMG_${System.currentTimeMillis()}.jpg"
        var savedImagePath: String? = null

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/ReportErrorApp")
        }

        val contentResolver = applicationContext.contentResolver
        val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        imageUri?.let { uri ->
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                savedImagePath = uri.toString()
            }
        }

        return savedImagePath
    }

    private fun sendReport() {
        val description = edtDescription.text.toString()

        if (description.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa una descripción", Toast.LENGTH_SHORT).show()
            return
        }

        if (imageBitmap == null) {
            Toast.makeText(this, "Por favor toma una foto primero", Toast.LENGTH_SHORT).show()
            return
        }

        // Generar Base64 de la imagen capturada
        val base64Image = encodeImageToBase64(imageBitmap!!)


        val reporte = ReporteMedicamentos(uuid,description,base64Image,"")

        viewModel.createReport(reporte)
    }

    private fun showAlertOk(context: Context, title: String, message: String) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                showMenuPrincipal()
            }
            .create()

        alertDialog.show()
    }


    private fun showMenuPrincipal(){
        edtDescription.text.clear()
        imgPreview.setImageResource(android.R.color.transparent)
    }

    private fun cleanBase64String(base64String: String): String {
        // Eliminar saltos de línea y barras invertidas
        return base64String.replace("\\n", "").replace("\\", "")
    }

    private fun encodeImageToBase64(bitmap: Bitmap): String {
        // Convertir la imagen a un ByteArrayOutputStream
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)  // Puedes ajustar la calidad
        val byteArray = byteArrayOutputStream.toByteArray()

        // Convertir el byte array a Base64 sin saltos de línea
        val base64String = Base64.encodeToString(byteArray, Base64.NO_WRAP)

        return cleanBase64String(base64String)
    }
}