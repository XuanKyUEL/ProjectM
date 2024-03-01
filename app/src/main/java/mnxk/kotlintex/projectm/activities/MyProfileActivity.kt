package mnxk.kotlintex.projectm.activities

import android.Manifest.*
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import mnxk.kotlintex.projectm.R
import mnxk.kotlintex.projectm.databinding.ActivityMyProfileBinding
import mnxk.kotlintex.projectm.firebase.fireStoreClass
import mnxk.kotlintex.projectm.models.User
import mnxk.kotlintex.projectm.utils.Constants

class MyProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityMyProfileBinding
    private var imageUri: Uri? = null
    private var profileImageURL: String = ""
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        fireStoreClass().loadUserData(this)
        binding.civProfileImage.setOnClickListener {
            openImageChooser()
        }
        binding.btnUpdate.setOnClickListener {
            if (imageUri != null) {
                uploadUserImage()
            } else {
                showProgessDialog("Please wait...")
                updateUserProfileData()
            }
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
                    imageUri = result.data!!.data!!
                    try {
                        Glide
                            .with(this)
                            .load(imageUri)
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
            actionBar.run {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            }
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
        this.user = user
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

    fun updateUserProfileData() {
        val userHashMap = HashMap<String, Any>()
        val name: String = binding.etName.text.toString().trim { it <= ' ' }
        if (name != user.name) {
            userHashMap[Constants.NAME] = name
        }
        val mobileNumber: Long = binding.etMobileNumber.text.toString().toLong()
        if (mobileNumber != user.mobile) {
            userHashMap[Constants.MOBILE] = mobileNumber
        }
        if (profileImageURL.isNotEmpty()) {
            userHashMap[Constants.IMAGE] = profileImageURL
        }
        if (userHashMap.isNotEmpty()) {
            fireStoreClass().updateUserProfileData(this, userHashMap)
        }
    }

    private fun getFileExtension(uri: Uri?): String? {
        return MimeTypeMap
            .getSingleton()
            .getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    private fun uploadUserImage() {
        showProgessDialog("Please wait...")
        if (imageUri != null) {
            val sRef: StorageReference =
                FirebaseStorage.getInstance().reference.child(
                    "USER_IMAGE" + System.currentTimeMillis() + "." + getFileExtension(imageUri),
                )

            sRef.putFile(imageUri!!).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { uri ->
                            Log.e("Downloadable Image URL", uri.toString())
                            profileImageURL = uri.toString()
                            updateUserProfileData()
                        }
                        .addOnFailureListener { exception ->
                            Log.e("Firebase Storage", "Failed to get download URL: ${exception.message}")
                        }
                } else {
                    Toast.makeText(this@MyProfileActivity, task.exception?.message, Toast.LENGTH_LONG).show()
                    Log.e("Image Upload Error", task.exception?.message, task.exception)
                    hideProgressDialog()
                }
            }
        }
    }

    fun userProfileUpdateSuccess(): Void? {
        hideProgressDialog()
        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
        finish()
        return null
    }
}
