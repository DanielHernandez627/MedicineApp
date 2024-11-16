package com.madicine.deliverycontrol.Entities

import java.io.Serializable

data class ReporteMedicamentos(var udi: String?,
                               var descripcion: String,
                               var imagen: String,
                               var fecha: String) : Serializable
