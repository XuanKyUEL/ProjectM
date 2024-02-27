package mnxk.kotlintex.projectm.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mnxk.kotlintex.projectm.databinding.ActivitySplashBinding
import mnxk.kotlintex.projectm.firebase.fireStoreClass

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    @OptIn(DelicateCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
        // Load the font
        val typeFace: Typeface = Typeface.createFromAsset(assets, "DFVN_BridgeType_Regular.ttf")
        binding.splashScreenTitle.typeface = typeFace

        val fireStoreClass = fireStoreClass()
        var currentUserId = fireStoreClass.getCurrentUserId()

        GlobalScope.launch {
            if (currentUserId.isNotEmpty()) {
                delay(2000L) // non-blocking delay for 2 seconds
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            } else {
                delay(2400L) // non-blocking delay for 2.4 seconds
                startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
                finish()
            }
        }
    }
}
