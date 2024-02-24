package mnxk.kotlintex.projectm.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import mnxk.kotlintex.projectm.R
import mnxk.kotlintex.projectm.databinding.ActivityMainBinding
import mnxk.kotlintex.projectm.databinding.AppBarMainBinding
import mnxk.kotlintex.projectm.databinding.NavHeaderMainBinding
import mnxk.kotlintex.projectm.models.User

class MainActivity :
    BaseActivity(),
    NavigationView.OnNavigationItemSelectedListener {
    private var binding: ActivityMainBinding? = null
    private var binding2: AppBarMainBinding? = null
    private var binding3: NavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding2 = binding?.appBarMain
        binding3 = binding?.navView
        setupActionBar()
        binding?.navView?.setNavigationItemSelectedListener(this)
//        setContent {
//            ProjectMTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background,
//                ) {
//                    Greeting("Android")
//                }
//            }
//        }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding2?.toolbar)
        binding2?.toolbar?.setNavigationIcon(R.drawable.ic_action_nav_on_menu)
        // Add click event to the navigation icon
        binding2?.toolbar?.setNavigationOnClickListener {
            // Navigate back to the previous activity
            toggleDrawer()
        }
    }

    private fun toggleDrawer() {
        if (binding?.drawerLayout?.isDrawerOpen(GravityCompat.START) == true) {
            binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        } else {
            binding?.drawerLayout?.openDrawer(GravityCompat.START)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (binding?.drawerLayout?.isDrawerOpen(GravityCompat.START) == true) {
            binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        } else {
            doubleBackToExit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_my_profile -> {
                Toast.makeText(this, "My Profile", Toast.LENGTH_SHORT).show()
                // Navigate to the My Profile screen
            }

            R.id.nav_sign_out -> {
                // Sign out the user
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
//                finish()
            }
        }
        binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }

    fun updateNavigationUserDetails(loggedInUser: User) {
        val viewHeader = binding?.navView?.getHeaderView(0)
        val headerBinding = viewHeader?.let { NavHeaderMainBinding.bind(it) }
        val imageViewfinder = headerBinding?.civUserImage
        headerBinding?.navHeaderMain.let {
            if (imageViewfinder != null) {
                Glide
                    .with(this)
                    .load(loggedInUser.image) // URL of the image
                    .centerCrop() // Scale type of the image.
                    .placeholder(R.drawable.ic_user_place_holder) // A default place holder
                    .into(imageViewfinder)
            } // the view in which the image will be loaded.
        } // the view in which the image will be loaded.

        headerBinding?.tvUserName?.text = loggedInUser.name
    }
}
