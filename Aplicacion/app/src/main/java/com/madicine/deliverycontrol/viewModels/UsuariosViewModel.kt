package com.madicine.deliverycontrol.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madicine.deliverycontrol.Repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UsuariosViewModel : ViewModel() {
    private val _respuesta = MutableLiveData<String?>()
    val respuesta: LiveData<String?> get() = _respuesta
    private val repository = UserRepository()

    //Funcion para la creacion de usuario
    fun crearUsuario(nombre: String, apellido: String, uuid: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.createUser(nombre, apellido, uuid)
            }
            _respuesta.value = result
        }
    }


}