package com.madicine.deliverycontrol.Repositories

import com.madicine.deliverycontrol.Services.MedicineService

class MedicamentoRepository {
    fun enviarCodigoBarras(codigoBarras: String, uUid: String): String? {
        return MedicineService.getMedicamentoByCodigo(codigoBarras, uUid)
    }
}