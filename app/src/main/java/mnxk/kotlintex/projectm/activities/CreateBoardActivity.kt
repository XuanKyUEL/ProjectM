package mnxk.kotlintex.projectm.activities

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mnxk.kotlintex.projectm.R
import mnxk.kotlintex.projectm.databinding.ActivityCreateBoardBinding
import mnxk.kotlintex.projectm.models.PermissionValidate

class CreateBoardActivity : BaseActivity() {
    private lateinit var binding: ActivityCreateBoardBinding
    private val permissionValidator = PermissionValidate(this)

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openImageChooser()
            } else {
                permissionValidator.requestPermission(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    PermissionValidate.READ_STORAGE_PERMISSION_REQUEST_CODE,
                )
            }
        }

    private val imagePickerActivityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    imageUri = result.data!!.data!!
                    try {
                        binding.civBoardImage.setImageURI(imageUri as Uri)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupActionBar()
        mainEvents()
    }

    override fun onImagePicked() {
        try {
            binding.civBoardImage.setImageURI(imageUri)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.run {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
                title = resources.getString(R.string.create_board_title)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun mainEvents() {
        binding.civBoardImage.setOnClickListener {
            openImageChooser()
        }
    }
}
