package mnxk.kotlintex.projectm.activities

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import mnxk.kotlintex.projectm.R
import mnxk.kotlintex.projectm.adapters.TaskListItemsAdapter
import mnxk.kotlintex.projectm.databinding.ActivityTaskListBinding
import mnxk.kotlintex.projectm.firebase.fireStoreClass
import mnxk.kotlintex.projectm.models.Board
import mnxk.kotlintex.projectm.models.Task
import mnxk.kotlintex.projectm.utils.Constants

class TaskListActivity : BaseActivity() {
    private lateinit var binding: ActivityTaskListBinding
    private lateinit var boardDetails: Board

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

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarTaskListActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.run {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
                title = boardDetails.name
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    fun boardDetails(board: Board) {
        boardDetails = board
        loadingDialog.dismissDialog()
        setUpActionBar()
        val addTaskList = Task(resources.getString(R.string.add_list))
        board.taskList.add(addTaskList)
        val adapter = TaskListItemsAdapter(this, board.taskList)
        binding.rvTaskList.layoutManager =
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false,
            )
        binding.rvTaskList.setHasFixedSize(true)
        binding.rvTaskList.adapter = adapter
    }

    fun addUpdateTaskListSuccess() {
        loadingDialog.dismissDialog()
        loadingDialog.startLoadingDialog("Loading board's tasks...")
        fireStoreClass().getBoardDetails(this, boardDetails.documentId)
    }

    fun createTaskList(taskListName: String) {
        val task = Task(taskListName, fireStoreClass().getCurrentUserId())
        boardDetails.taskList.add(0, task)
        boardDetails.taskList.removeAt(boardDetails.taskList.size - 1)
        loadingDialog.startLoadingDialog("Creating task list...")
        fireStoreClass().addUpdateTaskList(this, boardDetails)
    }
}
