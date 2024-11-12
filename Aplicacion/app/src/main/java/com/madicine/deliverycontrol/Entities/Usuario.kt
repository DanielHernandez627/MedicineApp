package com.madicine.deliverycontrol.Entities

import java.io.Serializable

data class Usuario(var udi: String?,
                   var nombre: String,
                   var apellido: String,
                   var email: String?,
                   var fechaRegistro: String?) : Serializable
