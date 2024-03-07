package mnxk.kotlintex.projectm.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import mnxk.kotlintex.projectm.R
import mnxk.kotlintex.projectm.adapters.TaskListItemsAdapter
import mnxk.kotlintex.projectm.databinding.ActivityTaskListBinding
import mnxk.kotlintex.projectm.firebase.fireStoreClass
import mnxk.kotlintex.projectm.models.Board
import mnxk.kotlintex.projectm.models.Card
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_members, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_members -> {
                val intent = Intent(this, MembersActivity::class.java)
                intent.putExtra(Constants.BOARD_DETAIL, boardDetails)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
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

    fun updateTaskList(
        position: Int,
        listName: String,
        model: Task,
    ) {
        val task = Task(listName, model.createdBy)
        boardDetails.taskList[position] = task
        boardDetails.taskList.removeAt(boardDetails.taskList.size - 1)
        loadingDialog.startLoadingDialog("Updating task list...")
        fireStoreClass().addUpdateTaskList(this, boardDetails)
    }

    fun deleteTaskList(position: Int) {
        boardDetails.taskList.removeAt(position)
        boardDetails.taskList.removeAt(boardDetails.taskList.size - 1)
        loadingDialog.startLoadingDialog("Deleting task list...")
        fireStoreClass().addUpdateTaskList(this, boardDetails)
    }

    fun addCardToTaskList(
        taskListPosition: Int,
        cardName: String,
    ) {
        boardDetails.taskList.removeAt(boardDetails.taskList.size - 1)
        val cardAssignedUsersList: ArrayList<String> = ArrayList()
        cardAssignedUsersList.add(fireStoreClass().getCurrentUserId())
        val card = Card(cardName, fireStoreClass().getCurrentUserId(), cardAssignedUsersList)
        val cardList = boardDetails.taskList[taskListPosition].cards
        cardList.add(card)
        val task =
            Task(
                boardDetails.taskList[taskListPosition].title,
                boardDetails.taskList[taskListPosition].createdBy,
                cardList,
            )
        boardDetails.taskList[taskListPosition] = task
        loadingDialog.startLoadingDialog("Adding card to task list...")
        fireStoreClass().addUpdateTaskList(this, boardDetails)
    }
}
