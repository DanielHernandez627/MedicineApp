package com.madicine.deliverycontrol.Repositories

import com.madicine.deliverycontrol.Services.medicineService

class medicamentoRepository {
    fun enviarCodigoBarras(codigoBarras: String, uUid: String): String? {
        return medicineService.getMedicamentoByCodigo(codigoBarras, uUid)
    }
}