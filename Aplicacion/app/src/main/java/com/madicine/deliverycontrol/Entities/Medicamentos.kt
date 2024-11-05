package com.madicine.deliverycontrol.Entities

import java.io.Serializable

data class Medicamentos(var nombre: String,
                        var concentracion: String,
                        var codigo: String,
                        var idMedicamento: String,
                        var idLaboratorio: String): Serializable
