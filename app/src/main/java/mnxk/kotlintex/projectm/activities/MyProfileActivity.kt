package mnxk.kotlintex.projectm.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
                loadingDialog.startLoadingDialog("Updating your profile...")
                updateUserProfileData()
            }
        }
    }

    override fun onImagePicked() {
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
        } else {
            finish()
        }
    }

    private fun uploadUserImage() {
        loadingDialog.startLoadingDialog("Uploading your profile picture...")
        if (imageUri != null) {
            val sRef: StorageReference =
                FirebaseStorage.getInstance().reference.child(
                    "USER_IMAGE" + System.currentTimeMillis() + "." + getFileExtension(this, imageUri),
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
                }
                loadingDialog.dismissDialog()
            }
        }
    }

    fun userProfileUpdateSuccess() {
        loadingDialog.dismissDialog()
        setResult(RESULT_OK)
        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
