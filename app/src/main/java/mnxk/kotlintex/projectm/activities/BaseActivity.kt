package mnxk.kotlintex.projectm.activities

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar.*
import com.google.firebase.auth.FirebaseAuth
import mnxk.kotlintex.projectm.R
import mnxk.kotlintex.projectm.databinding.ActivityBaseBinding
import mnxk.kotlintex.projectm.databinding.DialogCustomProgressBinding
import java.util.concurrent.Executors.newSingleThreadScheduledExecutor

open class BaseActivity : AppCompatActivity() {
    // A double back press to exit the app
    var doubleBackToExitPressedOnce = false

    lateinit var mProgressDialog: Dialog
    private var binding: ActivityBaseBinding? = null

//    private var dialogBinding = DialogCustomProgressBinding.inflate(layoutInflater)
    lateinit var dialogBinding: DialogCustomProgressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_base)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        dialogBinding = DialogCustomProgressBinding.inflate(layoutInflater)
    }

    protected fun isProgressDialogInitialized() = ::mProgressDialog.isInitialized

    fun showProgessDialog(text: String) {
        Log.d(TAG, "showProgessDialog() called")
        if (::mProgressDialog.isInitialized && mProgressDialog.isShowing) {
            dialogBinding.tvMessage.text = text
            Log.d(TAG, "Dialog is already showing")
            return
        }

        mProgressDialog = Dialog(this)
        mProgressDialog.setContentView(dialogBinding.root)
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        dialogBinding.tvMessage.text = text

        // Check if dialogBinding.root already has a parent
        val parent = dialogBinding.root.parent as? ViewGroup
        parent?.removeView(dialogBinding.root)

        mProgressDialog.show()
        Log.d(TAG, "Dialog is showing")
    }

//    fun showProgressDialog(text: String) {
//        if (!::mProgressDialog.isInitialized || !mProgressDialog.isShowing) {
//            mProgressDialog = Dialog(this)
//            mProgressDialog.setContentView(dialogBinding.root)
//            mProgressDialog.setCancelable(false)
//            mProgressDialog.setCanceledOnTouchOutside(false)
//            dialogBinding.tvMessage.text = text
//
//            // Hiển thị Dialog lên màn hình
//            mProgressDialog.show()
//        }
//    }

    fun getCurrentUserId(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    private var executor = newSingleThreadScheduledExecutor()

    fun doubleBackToExit() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(
            this,
            getString(R.string.please_click_back_again_to_exit),
            Toast.LENGTH_SHORT,
        ).show()
//        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
//        executor.schedule({ doubleBackToExitPressedOnce = false }, 2000, MILLISECONDS)
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    fun showErrorSnackBar(message: String) {
        val snackBar =
            make(
                findViewById(android.R.id.content),
                message,
                LENGTH_LONG,
            )
        val snackbarView = snackBar.view
        snackbarView.setBackgroundColor(getColor(R.color.snackbar_error_background))
        snackBar.show()
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun getFileExtension(
        activity: Activity,
        uri: Uri?,
    ): String? {
        return MimeTypeMap
            .getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

    protected var imageUri: Uri? = null

    private val imagePickerActivityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    imageUri = result.data!!.data!!
                    onImagePicked()
                }
            }
        }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openImageChooser()
            } else {
                Toast.makeText(this, "Permission required to access images.", Toast.LENGTH_SHORT).show()
            }
        }

    protected open fun onImagePicked() {
        // This method can be overridden in child classes to handle the image picked event
    }

    protected fun openImageChooser() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES,
            ) == PackageManager.PERMISSION_GRANTED -> {
                val intent =
                    Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "image/*"
                    }
                imagePickerActivityResult.launch(intent)
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
        }
    }
}
