package com.madicine.deliverycontrol.Interfaces

import android.app.Activity

interface PermissionHandler {
    // Verifica si el permiso de la cámara está otorgado
    fun verifyCameraPermission(activity: Activity): Boolean

    // Solicita el permiso de la cámara
    fun requestCameraPermission(activity: Activity)

    // Verifica si los permisos de almacenamiento están otorgados
    fun verifyStoragePermissions(activity: Activity): Boolean

    // Solicita los permisos de almacenamiento
    fun requestStoragePermissions(activity: Activity)

    // Obtiene los permisos de almacenamiento requeridos según la versión de Android
    fun getRequiredStoragePermissions(): Array<String>
}
