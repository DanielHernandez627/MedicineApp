package com.madicine.deliverycontrol.Repositories

import com.madicine.deliverycontrol.Entities.ReporteMedicamentos
import com.madicine.deliverycontrol.Services.ReportService

class ReportRepository {
    fun createReport(reporteMedicamentos: ReporteMedicamentos): String? {
        return ReportService.createReport(reporteMedicamentos)
    }
}