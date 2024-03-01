package mnxk.kotlintex.projectm.activities

import android.Manifest.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import mnxk.kotlintex.projectm.R
import mnxk.kotlintex.projectm.databinding.ActivityMyProfileBinding
import mnxk.kotlintex.projectm.firebase.fireStoreClass
import mnxk.kotlintex.projectm.models.User

class MyProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityMyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        fireStoreClass().loadUserData(this)
        binding.civProfileImage.setOnClickListener {
            openImageChooser()
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_IMAGE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                if (data != null) {
//                    try {
//                        val selectedImageUri = data.data!!
//                        Glide
//                            .with(this)
//                            .load(selectedImageUri)
//                            .centerCrop()
//                            .placeholder(R.drawable.ic_user_place_holder)
//                            .into(binding.civProfileImage)
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//        }
//    }
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openImageChooser()
            } else {
                Toast.makeText(this, "Permission required to access images.", Toast.LENGTH_SHORT).show()
            }
        }

    private val imagePickerActivityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    try {
                        val selectedImageUri = result.data!!.data!!
                        Glide
                            .with(this)
                            .load(selectedImageUri)
                            .centerCrop()
                            .placeholder(R.drawable.ic_user_place_holder)
                            .into(binding.civProfileImage)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

    private fun openImageChooser() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                permission.READ_MEDIA_IMAGES,
            ) == PackageManager.PERMISSION_GRANTED -> {
                val intent =
                    Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "image/*"
                    }
                imagePickerActivityResult.launch(intent)
            }
            else -> {
                requestPermissionLauncher.launch(permission.READ_MEDIA_IMAGES)
            }
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.my_profile_title)
        }
//        binding.toolbar.setOnClickListener {
// //            onBackPressed()
//            // Navigate back to the previous activity
//            finish()
//            Log.d("MyProfileActivity", "Back button is clicked.")
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        Log.d("MyProfileActivity", "Back button is clicked.")
        return true
    }

    fun setUserDataInUI(user: User) {
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(binding.civProfileImage)
        binding.etName.setText(user.name)
        binding.etEmail.setText(user.email)
        if (user.mobile != 0L) {
            binding.etMobileNumber.setText(user.mobile.toString())
        }
        // Set action bar title to the user's name
        supportActionBar?.title = "${user.name}'s Profile"
    }
}
