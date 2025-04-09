package com.madicine.deliverycontrol.Services

import android.content.Context
import android.content.SharedPreferences
import com.madicine.deliverycontrol.Repositories.SharedPrefsRepository

class SharedPrefsService(context: Context) : SharedPrefsRepository {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("com.madicine.deliverycontrol.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)

    override fun getString(key: String): String? = prefs.getString(key, null)

    override fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    override fun contains(key: String): Boolean = prefs.contains(key)

    override fun clear() {
        prefs.edit().clear().apply()
    }
}
