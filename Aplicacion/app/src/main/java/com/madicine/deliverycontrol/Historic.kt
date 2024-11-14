package com.madicine.deliverycontrol

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.madicine.deliverycontrol.Entities.Medicamentos
import com.madicine.deliverycontrol.Entities.MedicamentosConsulta
import com.madicine.deliverycontrol.viewModels.HistorialViewModel


class Historic : AppCompatActivity() {

    private lateinit var uuid : String
    private var medicamentos: List<MedicamentosConsulta>? = null
    private var listaMedicamentos: List<Resultado>? = null

    //Declaracion de variable ViewModel
    private val viewModel: HistorialViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historic)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        uuid = intent.getStringExtra("uuid").toString()
        viewModel.obtenerHistorial(uuid)

        //Lectura de ViewModel
        viewModel.historial.observe(this, Observer { respuesta ->
            respuesta?.let {
                respuesta.let {
                    medicamentos = it.medicamentos
                    showMedicine()
                }
            } ?: run {
                Log.d("MenuPrincipal","Error al obtener la respuesta")
            }
        })

    }

    data class Resultado(val titulo: String, val fecha: String)

    private fun showMedicine(){
        listaMedicamentos = medicamentos?.map {
            Resultado(titulo = it.nombreMedicamento, fecha = it.fechaConsulta)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = listaMedicamentos?.let { ResultadoAdapter(it) }
    }

    // Adapter interno
    inner class ResultadoAdapter(private val resultados: List<Resultado>) :
        RecyclerView.Adapter<ResultadoAdapter.ResultadoViewHolder>() {

        inner class ResultadoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val titulo: TextView = view.findViewById(R.id.tlHMedicamento)
            val fecha: TextView = view.findViewById(R.id.tlHFecha)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultadoViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_historial, parent, false)
            return ResultadoViewHolder(view)
        }

        override fun onBindViewHolder(holder: ResultadoViewHolder, position: Int) {
            val resultado = resultados[position]
            holder.titulo.text = "• ${resultado.titulo}"
            holder.fecha.text = resultado.fecha
        }

        override fun getItemCount() = resultados.size
    }
}