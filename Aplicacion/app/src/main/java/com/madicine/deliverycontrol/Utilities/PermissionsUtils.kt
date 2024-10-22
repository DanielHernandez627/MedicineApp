package com.madicine.deliverycontrol.Utilities

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.madicine.deliverycontrol.Interfaces.PermissionHandler

object PermissionsUtils : PermissionHandler {

    private const val CAMERA_PERMISSION_REQUEST_CODE = 100

    override fun verifyCameraPermission(activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.CAMERA
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    override fun requestCameraPermission(activity: Activity){
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }
}