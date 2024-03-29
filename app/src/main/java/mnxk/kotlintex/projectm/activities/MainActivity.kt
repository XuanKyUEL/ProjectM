package mnxk.kotlintex.projectm.activities

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import mnxk.kotlintex.projectm.R
import mnxk.kotlintex.projectm.adapters.BoardItemsAdapter
import mnxk.kotlintex.projectm.databinding.ActivityMainBinding
import mnxk.kotlintex.projectm.databinding.AppBarMainBinding
import mnxk.kotlintex.projectm.databinding.NavHeaderMainBinding
import mnxk.kotlintex.projectm.firebase.fireStoreClass
import mnxk.kotlintex.projectm.models.Board
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
                fireStoreClass.loadUserData(this, true)
            } else {
                Log.e("Cancelled", "Cancelled with result code: ${result.resultCode} and intent data: ${result.data}")
            }
        }

    private var backPressedTime: Long = 0

    private lateinit var userName: String
    private val boardLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val fireStoreClass = fireStoreClass()
                fireStoreClass.getBoardList(this)
            }
        }

    private val backPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding?.drawerLayout?.isDrawerOpen(GravityCompat.START) == true) {
                    binding?.drawerLayout?.closeDrawer(GravityCompat.START)
                } else {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - backPressedTime > 2000) {
                        backPressedTime = currentTime
                        Toast.makeText(this@MainActivity, "Press back again to exit", Toast.LENGTH_SHORT).show()
                    } else {
                        finishAffinity() // Đóng tất cả các activity
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding2 = binding?.appBarMain
        binding3 = binding?.navView
        setupActionBar()
        binding?.navView?.setNavigationItemSelectedListener(this)
        onBackPressedDispatcher.addCallback(this, backPressedCallback)
        val fireStoreClass = fireStoreClass()
        fireStoreClass.checkLoggedInUser(this)
        binding2?.fabCreateBoard?.setOnClickListener {
            val intent = Intent(this, CreateBoardActivity::class.java)
            intent.putExtra(Constants.NAME, userName)
            boardLauncher.launch(intent)
        }
    }

    fun populateBoardsListToUI(boardsList: ArrayList<Board>) {
        val reCycleView = binding?.appBarMain?.mainContent?.rvBoardsList ?: return
        val noBoardsAvailable = binding?.appBarMain?.mainContent?.tvNoBoardsAvailable ?: return
        if (boardsList.size > 0) {
            reCycleView.visibility = View.VISIBLE
            noBoardsAvailable.visibility = View.GONE
            // layout manager
            reCycleView.layoutManager = LinearLayoutManager(this)
            reCycleView.setHasFixedSize(true)
            // adapter
            val adapter = BoardItemsAdapter(this, boardsList)
            binding?.appBarMain?.mainContent?.rvBoardsList?.adapter = adapter
            // set on click listener
            adapter.setOnClickListener(
                object : BoardItemsAdapter.OnClickListener {
                    override fun onClick(
                        position: Int,
                        model: Board,
                    ) {
                        val intent = Intent(this@MainActivity, TaskListActivity::class.java)
                        intent.putExtra(Constants.DOCUMENT_ID, model.documentId)
                        startActivity(intent)
                    }
                },
            )

            // divider
            val divider = androidx.recyclerview.widget.DividerItemDecoration(this, LinearLayoutManager(this).orientation)
            reCycleView.addItemDecoration(divider)
        } else {
            binding?.appBarMain?.mainContent?.tvNoBoardsAvailable?.visibility = View.VISIBLE
            binding?.appBarMain?.mainContent?.rvBoardsList?.visibility = View.GONE
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
        Log.d(TAG, "signInActivity is destroyed.")
        if (binding?.drawerLayout?.isDrawerOpen(GravityCompat.START) == true) {
            binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        } else {
            Toast.makeText(this, "Sign out user successfully", Toast.LENGTH_SHORT).show()
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

    fun updateNavigationUserDetails(
        loggedInUser: User,
        readBoardsList: Boolean,
    ) {
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
            }
            if (readBoardsList) {
                loadingDialog.startLoadingDialog("Loading your boards...")
                Log.d("Dialog", "Loading your boards...")
                val fireStoreClass = fireStoreClass()
                fireStoreClass.getBoardList(this)
                loadingDialog.dismissDialog()
                Log.d("Dialog", "Close Loading screen...")
            }
            // the view in which the image will be loaded.
        } // the view in which the image will be loaded.
        Log.d("MainActivity", "updateNavigationUserDetails is called with user: ${loggedInUser.name}.")
        headerBinding?.tvUserName?.text = loggedInUser.name
    }
}
