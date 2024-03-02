package mnxk.kotlintex.projectm.models

import android.app.Activity

class PermissionValidate(private val activity: Activity) {
    fun checkPermission(permission: String): Boolean {
        return activity.checkSelfPermission(permission) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(
        permission: String,
        requestCode: Int,
    ) {
        activity.requestPermissions(arrayOf(permission), requestCode)
    }

    fun isPermissionGranted(grantResults: IntArray): Boolean {
        return grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        return activity.shouldShowRequestPermissionRationale(permission)
    }

    companion object {
        const val CAMERA_PERMISSION_REQUEST_CODE = 1
        const val READ_STORAGE_PERMISSION_REQUEST_CODE = 2
        const val PICK_IMAGE_REQUEST_CODE = 3
    }
}
