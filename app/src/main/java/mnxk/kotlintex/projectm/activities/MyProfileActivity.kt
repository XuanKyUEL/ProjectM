package mnxk.kotlintex.projectm.activities

import android.os.Bundle
import mnxk.kotlintex.projectm.databinding.ActivityMyProfileBinding

class MyProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityMyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
