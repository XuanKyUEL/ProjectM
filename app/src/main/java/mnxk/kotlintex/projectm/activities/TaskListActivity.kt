package mnxk.kotlintex.projectm.activities

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.google.firebase.firestore.DocumentSnapshot
import mnxk.kotlintex.projectm.R
import mnxk.kotlintex.projectm.databinding.ActivityTaskListBinding
import mnxk.kotlintex.projectm.firebase.fireStoreClass
import mnxk.kotlintex.projectm.models.Board
import mnxk.kotlintex.projectm.utils.Constants

class TaskListActivity : BaseActivity() {
    private lateinit var binding: ActivityTaskListBinding

    private val onBackPressed =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var boardDocumentId = ""
        if (intent.hasExtra("documentId")) {
            boardDocumentId = intent.getStringExtra(Constants.DOCUMENT_ID)!!
        }
        loadingDialog.startLoadingDialog("Loading board's tasks...")
        fireStoreClass().getBoardDetails(this, boardDocumentId)
    }

    private fun setUpActionBar(board: Board) {
        setSupportActionBar(binding.toolbarTaskListActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.run {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
                title = board.name
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    fun boardDetails(board: Board): DocumentSnapshot? {
        loadingDialog.dismissDialog()
        setUpActionBar(board)
        return null
    }
}
