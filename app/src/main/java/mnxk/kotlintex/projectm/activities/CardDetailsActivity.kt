package mnxk.kotlintex.projectm.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import mnxk.kotlintex.projectm.databinding.ActivityCardDetailsBinding

class CardDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCardDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCardDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
