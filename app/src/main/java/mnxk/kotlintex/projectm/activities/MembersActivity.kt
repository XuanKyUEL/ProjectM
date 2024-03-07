package mnxk.kotlintex.projectm.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mnxk.kotlintex.projectm.R
import mnxk.kotlintex.projectm.databinding.ActivityMembersBinding
import mnxk.kotlintex.projectm.models.Board

class MembersActivity : AppCompatActivity() {
    private lateinit var boardDetails: Board
    private lateinit var binding: ActivityMembersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMembersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (intent.hasExtra("boardDetail")) {
            boardDetails = intent.getParcelableExtra("boardDetail")!!
        }
        setupActionBar()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarMembersActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.run {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
                title = resources.getString(R.string.members)
            }
        }
        binding.toolbarMembersActivity.setNavigationOnClickListener { onBackPressed() }
    }
}
