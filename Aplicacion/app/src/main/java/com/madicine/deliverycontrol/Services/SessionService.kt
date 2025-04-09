package com.madicine.deliverycontrol.Services

import com.madicine.deliverycontrol.Repositories.SessionRepository
import com.madicine.deliverycontrol.Repositories.SharedPrefsRepository

class SessionService(private val sharedPrefs: SharedPrefsRepository) : SessionRepository {

    override fun saveSessionData(nombre: String, email: String, uid: String) {
        if (!sharedPrefs.contains("nombre")) sharedPrefs.putString("nombre", nombre)
        if (!sharedPrefs.contains("email")) sharedPrefs.putString("email", email)
        if (!sharedPrefs.contains("uid")) sharedPrefs.putString("uid", uid)
    }

    override fun clearSession() {
        sharedPrefs.clear()
    }

    override fun getNombre(): String = sharedPrefs.getString("nombre") ?: ""
    override fun getEmail(): String = sharedPrefs.getString("email") ?: ""
    override fun getUid(): String = sharedPrefs.getString("uid") ?: ""
}
