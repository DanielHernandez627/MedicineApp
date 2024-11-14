package com.madicine.deliverycontrol.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madicine.deliverycontrol.Entities.HistorialMedicamentos
import com.madicine.deliverycontrol.Entities.MedicamentosConsulta
import com.madicine.deliverycontrol.Repositories.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class HistorialViewModel : ViewModel() {

    private val _historial = MutableLiveData<HistorialMedicamentos?>()
    val historial: LiveData<HistorialMedicamentos?> get() = _historial

    private val repository = HistoryRepository()


    //Funcion para la busqueda de historial en base UID del usuario
    fun obtenerHistorial(uuid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val jsonString = repository.searchHistoryByUuid(uuid)
            jsonString?.let {
                val jsonObject = JSONObject(it)

                //Nombre y Apellido
                val nombre = jsonObject.getString("Nombre")
                val apellido = jsonObject.getString("Apellido")

                //Generacion lista de medicamentos
                val medicamentosArray = jsonObject.getJSONArray("Medicamentos")
                val listaMedicamentos = mutableListOf<MedicamentosConsulta>()

                for (i in 0 until medicamentosArray.length()) {
                    val medicamentoObject = medicamentosArray.getJSONObject(i)

                    val medicamento = MedicamentosConsulta(
                        idMedicamento = medicamentoObject.getInt("Id_Medicamento"),
                        nombreMedicamento = medicamentoObject.getString("NombreMedicamento"),
                        fechaConsulta = medicamentoObject.getString("FechaConsulta")
                    )

                    listaMedicamentos.add(medicamento)
                }

                //Creacion de objeto de tipo historial
                val historial = HistorialMedicamentos(
                    nombre = nombre,
                    apellido = apellido,
                    medicamentos = listaMedicamentos)

                _historial.postValue(historial)

            }
        }
    }

}