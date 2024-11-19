package com.madicine.deliverycontrol.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madicine.deliverycontrol.Entities.Medicamentos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import com.madicine.deliverycontrol.Repositories.MedicamentoRepository
import org.json.JSONObject

class MedicamentosViewModel : ViewModel() {
    private val _respuesta = MutableLiveData<String?>()
    val respuesta: LiveData<String?> get() = _respuesta
    private val repository = MedicamentoRepository()
    private val _medicamento = MutableLiveData<Medicamentos?>()
    val medicamento: LiveData<Medicamentos?> get() = _medicamento

    fun enviarCodigoBarras(codigoBarras: String, uUid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val jsonString = repository.enviarCodigoBarras(codigoBarras, uUid)

            jsonString?.let {
                // Parse the JSON response
                val jsonObject = JSONObject(it)

                val medicamento = Medicamentos(
                    idMedicamento = jsonObject.getString("Id_Medicamento"),
                    nombre = jsonObject.getString("Nombre"),
                    concentracion = jsonObject.getString("Concentracion"),
                    codigo = jsonObject.getString("CodigoBarras"),
                    idLaboratorio = jsonObject.getString("Id_Laboratorio"),
                    tipoMedicamento = jsonObject.getString("TipoMedicamento"),
                    contraIndicacion = jsonObject.getString("ContraIndicacion"),
                    nombreLaboratorio = jsonObject.getString("NombreLaboratorio")
                )

                _medicamento.postValue(medicamento)
            } ?: run {
                _medicamento.postValue(null)
            }
        }
    }
}