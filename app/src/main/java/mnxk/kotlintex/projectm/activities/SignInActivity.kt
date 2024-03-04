package mnxk.kotlintex.projectm.activities

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
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
        val email = intent.getStringExtra("email")
        binding.etEmailSignIn.setText(email)

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

    override fun onBackPressed() {
        val sharedPref = getSharedPreferences("backPressState", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("isFromSignInOrSignUp", true)
            apply()
        }
        super.onBackPressed()
    }

    private fun signInUser() {
        val email = binding.etEmailSignIn.text.toString().trim { it <= ' ' }
        val password = binding.etPasswordSignIn.text.toString().trim { it <= ' ' }

        if (validateUserDetails()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Log.d(TAG, "signInWithEmail:success")
                    auth.currentUser
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "signInWithEmail:failure", exception)
                    when (exception) {
                        is FirebaseAuthInvalidUserException -> {
                            Toast.makeText(
                                baseContext,
                                "This email is not registered. Please sign up first.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            Toast.makeText(
                                baseContext,
                                "Email or password is incorrect. Please try again.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                        else -> {
                            Toast.makeText(
                                baseContext,
                                "Sign in failed. Please try again later.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
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
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
