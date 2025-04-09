package com.madicine.deliverycontrol.Repositories

interface SharedPrefsRepository {
    fun getString(key: String): String?
    fun putString(key: String, value: String)
    fun contains(key: String): Boolean
    fun clear()
}
