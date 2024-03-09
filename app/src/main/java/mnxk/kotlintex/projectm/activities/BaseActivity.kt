package mnxk.kotlintex.projectm.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar.make
import com.google.firebase.auth.FirebaseAuth
import mnxk.kotlintex.projectm.R
import mnxk.kotlintex.projectm.databinding.ActivityBaseBinding

open class BaseActivity : AppCompatActivity() {
    private var binding: ActivityBaseBinding? = null

    var loadingDialog = LoadingDialog(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_base)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }

    fun getCurrentUserId(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
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
        // Hide keyboard
        val inputMethod = getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        val currentFocusedView = currentFocus
        if (currentFocusedView != null) {
            inputMethod.hideSoftInputFromWindow(currentFocusedView.windowToken, 0)
        }
        snackBar.show()
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
