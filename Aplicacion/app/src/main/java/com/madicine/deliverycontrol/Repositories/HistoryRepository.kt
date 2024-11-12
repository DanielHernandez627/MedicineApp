package com.madicine.deliverycontrol.Repositories

import com.madicine.deliverycontrol.Services.HistoryService

class HistoryRepository {

    fun searchHistoryByUuid(uuid: String): String? {
        return HistoryService.searchHistoryByUuid(uuid)
    }


}