package mnxk.kotlintex.projectm.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import mnxk.kotlintex.projectm.R
import mnxk.kotlintex.projectm.databinding.ActivityMainBinding
import mnxk.kotlintex.projectm.databinding.AppBarMainBinding
import mnxk.kotlintex.projectm.databinding.NavHeaderMainBinding
import mnxk.kotlintex.projectm.firebase.fireStoreClass
import mnxk.kotlintex.projectm.models.User
import mnxk.kotlintex.projectm.utils.Constants

class MainActivity :
    BaseActivity(),
    NavigationView.OnNavigationItemSelectedListener {
    private var binding: ActivityMainBinding? = null
    private var binding2: AppBarMainBinding? = null
    private var binding3: NavigationView? = null

    private val startForResult =
        this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val fireStoreClass = fireStoreClass()
                fireStoreClass.loadUserData(this)
            } else {
                Log.e("Cancelled", "Cancelled with result code: ${result.resultCode} and intent data: ${result.data}")
            }
        }

    private lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding2 = binding?.appBarMain
        binding3 = binding?.navView
        setupActionBar()
        binding?.navView?.setNavigationItemSelectedListener(this)
        val fireStoreClass = fireStoreClass()
        fireStoreClass.checkLoggedInUser(this)
        binding2?.fabCreateBoard?.setOnClickListener {
            val intent = Intent(this, CreateBoardActivity::class.java)
            intent.putExtra(Constants.NAME, userName)
            startActivity(intent)
        }
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
            if (isProgressDialogInitialized()) {
                doubleBackToExit()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_my_profile -> {
                // Navigate to the My Profile screen
                val intent = Intent(this, MyProfileActivity::class.java)
                startForResult.launch(intent)
            }

            R.id.nav_sign_out -> {
                // Sign out the user
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
        }
        binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }

    fun updateNavigationUserDetails(loggedInUser: User) {
        Log.d("MainActivity", "updateNavigationUserDetails is called with user: ${loggedInUser.name}.")

        val viewHeader = binding?.navView?.getHeaderView(0)
        val headerBinding = viewHeader?.let { NavHeaderMainBinding.bind(it) }
        val imageViewfinder = headerBinding?.civUserImage
        userName = loggedInUser.name
        headerBinding?.navHeaderMain.let {
            if (imageViewfinder != null) {
                Log.d("MainActivity", "updateNavigationUserDetails is called with user: ${loggedInUser.id}.")
                Glide
                    .with(this)
                    .load(loggedInUser.image) // URL of the image
                    .centerCrop() // Scale type of the image.
                    .placeholder(R.drawable.ic_user_place_holder) // A default place holder
                    .into(imageViewfinder)
            } // the view in which the image will be loaded.
        } // the view in which the image will be loaded.
        Log.d("MainActivity", "updateNavigationUserDetails is called with user: ${loggedInUser.name}.")
        headerBinding?.tvUserName?.text = loggedInUser.name
    }
}
