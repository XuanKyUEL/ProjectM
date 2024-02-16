package mnxk.kotlintex.projectm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import mnxk.kotlintex.projectm.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sign_up)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
    }

    // Todo: setup action bar
    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarSignUp)
        val actionBar = supportActionBar
        if (actionBar != null) {
            // Show the back button in action bar
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
    }

    // Todo: Handle the back button click
    // onBackPressed() is called when the back button of the action bar is clicked
    // but it is deprecated
//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
//        return true
//    }
    override fun onSupportNavigateUp(): Boolean {
        // Navigate back to the previous activity
        // finish() is used to destroy the current activity
        finish()
        return true
    }
}
