package com.madicine.deliverycontrol.Repositories

import com.madicine.deliverycontrol.Services.UserService

class UserRepository {

    fun createUser(nombre:String,apellido:String,uuid:String): String? {
        return UserService.createUser(nombre,apellido,uuid)
    }

    fun searchUser(uuid: String): String? {
        return UserService.searchUser(uuid)
    }
}