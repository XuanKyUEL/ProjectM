package mnxk.kotlintex.projectm

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import mnxk.kotlintex.projectm.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
//        setContentView(R.layout.activity_splash)
        // FLAG_FULLSCREEN is deprecated
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//        )
        // Use the following code instead of the deprecated FLAG_FULLSCREEN
        // This method is used to hide the status bar and make
        // the splash screen as a full screen activity.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
            )
        }
//        window.insetsController?.hide(WindowInsets.Type.statusBars())
        // Load the font
        val typeFace: Typeface = Typeface.createFromAsset(assets, "DFVN_BridgeType_Regular.ttf")
        binding.splashScreenTitle.typeface = typeFace
    }
}
