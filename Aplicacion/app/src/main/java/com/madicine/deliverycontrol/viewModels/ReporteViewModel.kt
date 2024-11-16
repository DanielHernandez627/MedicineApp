package com.madicine.deliverycontrol.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madicine.deliverycontrol.Entities.ReporteMedicamentos
import com.madicine.deliverycontrol.Repositories.ReportRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReporteViewModel : ViewModel() {

    private val _isReportCreated = MutableLiveData<Boolean>()
    val isReportCreated: LiveData<Boolean> get() = _isReportCreated

    private val reportRepository = ReportRepository()

    // Función para crear el reporte y verificar si fue exitoso
    fun createReport(reporteMedicamentos: ReporteMedicamentos) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = reportRepository.createReport(reporteMedicamentos)
            val isSuccess = response != null
            _isReportCreated.postValue(isSuccess)
        }
    }

}