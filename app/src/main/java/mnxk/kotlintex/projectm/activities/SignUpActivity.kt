package mnxk.kotlintex.projectm.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import mnxk.kotlintex.projectm.R
import mnxk.kotlintex.projectm.databinding.ActivitySignUpBinding
import mnxk.kotlintex.projectm.firebase.fireStoreClass
import mnxk.kotlintex.projectm.models.User

class SignUpActivity : BaseActivity() {
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
        binding.btnSignUp.setOnClickListener {
            registerUser()
        }
    }

    override fun onBackPressed() {
        val sharedPref = getSharedPreferences("backPressState", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("isFromSignInOrSignUp", true)
            apply()
        }
        super.onBackPressed()
    }

    fun userRegistrationSuccess() {
        Toast.makeText(
            this,
            "You have successfully registered",
            Toast.LENGTH_SHORT,
        ).show()
        hideProgressDialog()
        // sign out the user
        FirebaseAuth.getInstance().signOut()
        // finish the SignUpActivity
        finish()
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

    private fun registerUser() {
        // Get the text from the EditText and remove the leading and trailing white spaces
        val name: String = binding.etNameSignUp.text.toString().trim { it <= ' ' }
        val email: String = binding.etEmailSignUp.text.toString().trim { it <= ' ' }
        val password: String = binding.etPasswordSignUp.text.toString().trim { it <= ' ' }
        val intent = Intent(this, SignInActivity::class.java)

        if (validateForm(name, email, password)) {
            showProgessDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val registeredEmail = firebaseUser.email!!
                        val user = User(firebaseUser.uid, name, registeredEmail)
                        fireStoreClass().registerUser(this, user)
                        // TODO : Navigate the user to signInActivity
                        intent.putExtra("email", email)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }

    private fun validateForm(
        name: String,
        email: String,
        password: String,
    ): Boolean {
        return when {
            name.isEmpty() -> {
                showErrorSnackBar("Please enter your name")
                false
            }
            email.isEmpty() -> {
                showErrorSnackBar("Please enter your email address")
                false
            }
            password.isEmpty() -> {
                showErrorSnackBar("Please enter your password")
                false
            }
            else -> {
                true
            }
        }
    }
}
