package mnxk.kotlintex.projectm.activities

import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import mnxk.kotlintex.projectm.R
import mnxk.kotlintex.projectm.adapters.MemberListItemsAdapter
import mnxk.kotlintex.projectm.databinding.ActivityMembersBinding
import mnxk.kotlintex.projectm.databinding.DialogSearchMemberBinding
import mnxk.kotlintex.projectm.firebase.fireStoreClass
import mnxk.kotlintex.projectm.models.Board
import mnxk.kotlintex.projectm.models.User
import mnxk.kotlintex.projectm.utils.Constants
import mnxk.kotlintex.projectm.utils.IntentUtils

class MembersActivity : BaseActivity() {
    private lateinit var boardDetails: Board
    private lateinit var binding: ActivityMembersBinding
    private lateinit var searchdialogBinding: DialogSearchMemberBinding
    private lateinit var assignedMembersDetailList: ArrayList<User>

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
        if (intent.hasExtra(Constants.BOARD_DETAIL)) {
            boardDetails = IntentUtils.getParcelableExtra<Board>(intent, Constants.BOARD_DETAIL)!!
            loadingDialog.startLoadingDialog("Loading members...")
            fireStoreClass().getAssignMemberListDetials(this, boardDetails.assignedTo)
        }
        setupActionBar()
    }

    fun setupMembersList(list: ArrayList<User>) {
        assignedMembersDetailList = list
        loadingDialog.dismissDialog()
        binding.rvMembersList.layoutManager = LinearLayoutManager(this)
        binding.rvMembersList.setHasFixedSize(true)
        val adapter = MemberListItemsAdapter(this, list)
        binding.rvMembersList.adapter = adapter
    }

    fun memberDetails(user: User) {
        boardDetails.assignedTo.add(user.id)
        fireStoreClass().assignMemberToBoard(this, boardDetails, user)
    }

    fun memberAssignSuccess(user: User) {
        assignedMembersDetailList.add(user)
        loadingDialog.dismissDialog()
        setupMembersList(assignedMembersDetailList)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_member, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_member -> {
                dialogSearchMember()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun dialogSearchMember() {
        val dialog = Dialog(this)
        searchdialogBinding = DialogSearchMemberBinding.inflate(layoutInflater)
        dialog.setContentView(searchdialogBinding.root)
        searchdialogBinding.tvAdd.setOnClickListener {
            val email = searchdialogBinding.etEmailSearchMember.text.toString()
            if (email.isNotEmpty()) {
                dialog.dismiss()
                loadingDialog.startLoadingDialog("Adding member...")
                fireStoreClass().getMemberDetails(this, email)
            } else {
                showErrorSnackBar("Please enter member's email address.")
                // hide the dialog and keyboard
                dialog.dismiss()
            }
        }
        searchdialogBinding.tvCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}
