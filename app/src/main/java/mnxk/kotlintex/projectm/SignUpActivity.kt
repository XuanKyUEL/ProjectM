package mnxk.kotlintex.projectm

import android.content.Context
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

    // Use the following code instead of the deprecated onBackPressed()
    override fun onSupportNavigateUp(): Boolean {
        // Navigate back to the previous activity
        // finish() is used to destroy the current activity
        finish()
        return true
    }

    // Save the user input when the back button is clicked
    override fun onPause() {
        super.onPause()
        val sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("name", binding.etNameSignUp.text.toString())
        editor.putString("email", binding.etEmailSignUp.text.toString())
        editor.putString("password", binding.etPasswordSignUp.text.toString())
        editor.apply()
    }

    // Restore the user input when the activity is created
//    override fun onResume() {
//        super.onResume()
//        val sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
//        binding.etNameSignUp.setText(sharedPref.getString("name", ""))
//        binding.etEmailSignUp.setText(sharedPref.getString("email", ""))
//        binding.etPasswordSignUp.setText(sharedPref.getString("password", ""))
//    }
}
