package mnxk.kotlintex.projectm.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import mnxk.kotlintex.projectm.R
import mnxk.kotlintex.projectm.databinding.ActivitySignInBinding
import mnxk.kotlintex.projectm.models.User

class SignInActivity : BaseActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnSignIn.setOnClickListener {
            signInUser()
        }

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

    private fun signInUser() {
        val email = binding.etEmailSignIn.text.toString().trim { it <= ' ' }
        val password = binding.etPasswordSignIn.text.toString().trim { it <= ' ' }

        if (validateUserDetails()) {
            showProgessDialog("Please wait...")
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }

    private fun validateUserDetails(): Boolean {
        return when {
            binding.etEmailSignIn.text.toString().isEmpty() -> {
                showErrorSnackBar("Please enter an email address.")
                false
            }
            binding.etPasswordSignIn.text.toString().isEmpty() -> {
                showErrorSnackBar("Please enter a password.")
                false
            }
            else -> {
                true
            }
        }
    }

    fun signInSuccess(loggedInUser: User) {
        hideProgressDialog()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
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
