package mnxk.kotlintex.projectm.activities

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import mnxk.kotlintex.projectm.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity() {
    private lateinit var binding: ActivityIntroBinding
    private var isBackPressed: Boolean = false
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_intro)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val typeFace = Typeface.createFromAsset(assets, "DFVN_BridgeType_Regular.ttf")
        binding.tvAppNameIntro.typeface = typeFace

        binding.btnSignUpIntro.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.btnSignInIntro.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }

    override fun onRestart() {
        super.onRestart()

        val sharedPref = getSharedPreferences("backPressState", Context.MODE_PRIVATE)
        val isFromSignInOrSignUp = sharedPref.getBoolean("isFromSignInOrSignUp", true)

        if (isFromSignInOrSignUp) {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({
                with(sharedPref.edit()) {
                    putBoolean("isFromSignInOrSignUp", false)
                    apply()
                }
            }, 2000)
        }
    }

    override fun onBackPressed() {
        val sharedPref = getSharedPreferences("backPressState", Context.MODE_PRIVATE)
        val isFromSignInOrSignUp = sharedPref.getBoolean("isFromSignInOrSignUp", false)

        if (isFromSignInOrSignUp) {
            super.onBackPressed()
        } else {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed()
                return
            } else {
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
            }
            backPressedTime = System.currentTimeMillis()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val sharedPref = getSharedPreferences("backPressState", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("isFromSignInOrSignUp", false)
            apply()
        }
    }
}
