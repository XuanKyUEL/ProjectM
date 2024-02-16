package mnxk.kotlintex.projectm.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import mnxk.kotlintex.projectm.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity() {
    private lateinit var binding: ActivityIntroBinding

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
}