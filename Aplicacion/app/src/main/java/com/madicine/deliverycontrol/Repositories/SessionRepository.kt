package com.madicine.deliverycontrol.Repositories

interface SessionRepository {
    fun saveSessionData(nombre: String, email: String, uid: String)
    fun clearSession()
    fun getNombre(): String
    fun getEmail(): String
    fun getUid(): String
}

