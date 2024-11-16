package com.madicine.deliverycontrol.Utilities

import android.app.Activity
import android.content.Context
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.madicine.deliverycontrol.Interfaces.PermissionHandler

object PermissionsUtils : PermissionHandler {

    private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    private const val STORAGE_PERMISSION_CODE = 1001

    // Método para obtener los permisos necesarios según la versión de Android
    override fun getRequiredStoragePermissions(): Array<String> = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
            // Android 14+
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            )
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
            // Android 13
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO
            )
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
            // Android 10-12
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
        else -> {
            // Android 9 y anteriores
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    // Verificar permiso de cámara
    override fun verifyCameraPermission(activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Solicitar permiso de cámara
    override fun requestCameraPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    // Verificar permisos de almacenamiento
    override fun verifyStoragePermissions(activity: Activity): Boolean {
        return getRequiredStoragePermissions().all { permission ->
            ContextCompat.checkSelfPermission(
                activity,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Solicitar permisos de almacenamiento
    override fun requestStoragePermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            getRequiredStoragePermissions(),
            STORAGE_PERMISSION_CODE
        )
    }

    // Método de utilidad para verificar si se otorgaron todos los permisos
    fun arePermissionsGranted(grantResults: IntArray): Boolean {
        return grantResults.isNotEmpty() &&
                grantResults.all { it == PackageManager.PERMISSION_GRANTED }
    }

    // Método de utilidad para verificar todos los permisos necesarios
    fun verifyAllPermissions(activity: Activity): Boolean {
        return verifyCameraPermission(activity) && verifyStoragePermissions(activity)
    }

    // Método de utilidad para solicitar todos los permisos necesarios
    fun requestAllPermissions(activity: Activity) {
        val permissionsToRequest = mutableListOf<String>()

        if (!verifyCameraPermission(activity)) {
            permissionsToRequest.add(Manifest.permission.CAMERA)
        }

        getRequiredStoragePermissions().forEach { permission ->
            if (ContextCompat.checkSelfPermission(activity, permission) !=
                PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission)
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                activity,
                permissionsToRequest.toTypedArray(),
                STORAGE_PERMISSION_CODE
            )
        }
    }
}