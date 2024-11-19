package com.madicine.deliverycontrol.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madicine.deliverycontrol.Entities.Usuario
import com.madicine.deliverycontrol.Repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class UsuariosViewModel : ViewModel() {
    private val _respuesta = MutableLiveData<String?>()
    val respuesta: LiveData<String?> get() = _respuesta
    private val repository = UserRepository()
    private val _usuario = MutableLiveData<Usuario?>()
    val usuario: LiveData<Usuario?> get() = _usuario

    //Funcion para la creacion de usuario
    fun crearUsuario(nombre: String, apellido: String, uuid: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.createUser(nombre, apellido, uuid)
            }
            _respuesta.value = result
        }
    }

    //Funcion para la busqueda de usuario
    fun buscarUsuario(uuid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val jsonString = repository.searchUser(uuid)
            jsonString?.let {
                val jsonObject = JSONObject(it)

                //Creacion de objeto de tipo usuario
                val usuario = Usuario(
                    udi = jsonObject.getString("U_Uid"),
                    nombre = jsonObject.getString("Nombre"),
                    apellido = jsonObject.getString("Apellido"),
                    email = null,
                    fechaRegistro = jsonObject.getString("FechaIngreso")
                )

                _usuario.postValue(usuario)
            }?: run {
                _usuario.postValue(null)
            }
        }
    }
}