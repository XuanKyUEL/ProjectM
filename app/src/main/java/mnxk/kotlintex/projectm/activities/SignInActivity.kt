package mnxk.kotlintex.projectm.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import mnxk.kotlintex.projectm.R
import mnxk.kotlintex.projectm.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarSignIn)
        val actionBar = supportActionBar
        if (actionBar != null) {
            // Show the back button in action bar
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

//    override fun onPause() {
//        super.onPause()
//        val sharedPref = getSharedPreferences("user_data_signin", Context.MODE_PRIVATE)
//        val editor = sharedPref.edit()
//        editor.putString("email", binding.etEmailSignIn.text.toString())
//        editor.putString("password", binding.etPasswordSignIn.text.toString())
//        editor.apply()
//    }
}
