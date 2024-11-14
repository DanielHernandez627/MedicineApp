package com.madicine.deliverycontrol

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.madicine.deliverycontrol.Entities.Medicamentos

class ViewMedicine : AppCompatActivity() {

    private lateinit var tl_nombre: TextView
    private lateinit var tl_concentracion: TextView
    private lateinit var tl_tipoMedicamento: TextView
    private lateinit var tl_contraIndicacion: TextView
    private lateinit var tl_nombreLaboratorio: TextView
    private var medicamento: Medicamentos? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_medicine)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tl_nombre = findViewById(R.id.tlMedicamento)
        tl_concentracion = findViewById(R.id.tlConcentracion)
        tl_tipoMedicamento = findViewById(R.id.tlTipo)
        tl_contraIndicacion = findViewById(R.id.tlPeligro)
        tl_nombreLaboratorio = findViewById(R.id.tlLaboratorio)

        //Obtencion de datos del medicamento put extra
        val bundle = intent.extras
        medicamento = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            bundle?.getSerializable("Medicamento", Medicamentos::class.java)
        }else{
            bundle?.getSerializable("Medicamento") as? Medicamentos
        }

        showData(medicamento)
    }

    private fun showData(medicamentos: Medicamentos?){
        tl_nombre.text = medicamentos?.nombre
        tl_concentracion.text = "Concentracion: ${medicamentos?.concentracion}"
        tl_tipoMedicamento.text = "Especialidad: ${medicamentos?.tipoMedicamento}"
        tl_contraIndicacion.text = "Riesgos: ${medicamentos?.contraIndicacion}"
        tl_nombreLaboratorio.text = "Laboratorio: ${medicamentos?.nombreLaboratorio}"
    }
}