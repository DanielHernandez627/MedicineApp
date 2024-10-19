package com.madicine.deliverycontrol.Interfaces

import android.app.Activity

interface PermissionHandler {
    fun verifyCameraPermission(activity: Activity) : Boolean
    fun requestCameraPermission(activity: Activity)
}