package mnxk.kotlintex.projectm.activities

import android.os.Bundle
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
