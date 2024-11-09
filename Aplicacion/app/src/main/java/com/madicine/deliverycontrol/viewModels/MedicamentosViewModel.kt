package com.madicine.deliverycontrol.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import com.madicine.deliverycontrol.Repositories.MedicamentoRepository

class MedicamentosViewModel : ViewModel() {
    private val _respuesta = MutableLiveData<String?>()
    val respuesta: LiveData<String?> get() = _respuesta
    private val repository = MedicamentoRepository()

    fun enviarCodigoBarras(codigoBarras: String, uUid: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.enviarCodigoBarras(codigoBarras, uUid)
            }
            _respuesta.value = result
        }
    }
}