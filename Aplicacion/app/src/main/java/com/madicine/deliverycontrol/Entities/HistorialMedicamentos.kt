package com.madicine.deliverycontrol.Entities

data class HistorialMedicamentos(val nombre: String,
                                 val apellido: String,
                                 val medicamentos: List<MedicamentosConsulta>)
