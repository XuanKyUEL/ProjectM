package mnxk.kotlintex.projectm.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import mnxk.kotlintex.projectm.activities.CreateBoardActivity
import mnxk.kotlintex.projectm.activities.MainActivity
import mnxk.kotlintex.projectm.activities.MembersActivity
import mnxk.kotlintex.projectm.activities.MyProfileActivity
import mnxk.kotlintex.projectm.activities.SignInActivity
import mnxk.kotlintex.projectm.activities.SignUpActivity
import mnxk.kotlintex.projectm.activities.TaskListActivity
import mnxk.kotlintex.projectm.models.Board
import mnxk.kotlintex.projectm.models.User
import mnxk.kotlintex.projectm.utils.Constants

class fireStoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(
        activity: SignUpActivity,
        userInfo: User,
    ) {
        mFireStore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.loadingDialog.dismissDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error writing document",
                    e,
                )
            }
    }

    fun checkLoggedInUser(activity: MainActivity) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)!!
                activity.updateNavigationUserDetails(loggedInUser, true)
            }
            .addOnFailureListener { e ->
                Log.e("SignInUser", "Error writing document", e)
            }
    }

    fun getCurrentUserId(): String {
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun updateUserProfileData(
        activity: MyProfileActivity,
        userHashMap: HashMap<String, Any>,
    ) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                activity.userProfileUpdateSuccess()
            }
            .addOnFailureListener { e ->
                activity.loadingDialog.dismissDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e,
                )
                Toast.makeText(
                    activity,
                    "Error when updating the user details.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
    }

    fun loadUserData(
        activity: Activity,
        readBoardsList: Boolean = false,
    ) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)!!

                when (activity) {
                    is SignInActivity -> {
                        activity.signInSuccess(loggedInUser)
                    }
                    is MainActivity -> {
                        activity.updateNavigationUserDetails(loggedInUser, readBoardsList)
                    }
                    is MyProfileActivity -> {
                        activity.setUserDataInUI(loggedInUser)
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is SignInActivity -> {
                        activity.loadingDialog.dismissDialog()
                    }
                    is MainActivity -> {
                        activity.loadingDialog.dismissDialog()
                    }
                    is MyProfileActivity -> {
                        activity.loadingDialog.dismissDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting loggedIn user details",
                    e,
                )
            }
    }

    fun createBoard(
        activity: CreateBoardActivity,
        board: Board,
    ) {
        mFireStore.collection(Constants.BOARDS)
            .document()
            .set(board, SetOptions.merge())
            .addOnSuccessListener {
                activity.boardCreatedSuccessfully()
            }
            .addOnFailureListener { e ->
                activity.loadingDialog.dismissDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating a board.", e)
                Toast.makeText(
                    activity,
                    "Error when creating a board.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
    }

    fun getBoardList(activity: MainActivity) {
        mFireStore.collection(Constants.BOARDS)
            .whereArrayContains(Constants.ASSIGNED_TO, getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())
                val boardList: ArrayList<Board> = ArrayList()
                for (i in document.documents) {
                    val board = i.toObject(Board::class.java)!!
                    board.documentId = i.id
                    boardList.add(board)
                }
                activity.populateBoardsListToUI(boardList)
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error writing document", e)
            }
    }

    fun getBoardDetails(
        activity: TaskListActivity,
        boardDocumentId: String,
    ) {
        mFireStore.collection(Constants.BOARDS)
            .document(boardDocumentId)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                val board = document.toObject(Board::class.java)!!
                board.documentId = document.id
                activity.boardDetails(board)
            }
            .addOnFailureListener { e ->
                activity.loadingDialog.dismissDialog()
                Log.e(activity.javaClass.simpleName, "Error writing document", e)
            }
    }

    fun addUpdateTaskList(
        activity: TaskListActivity,
        board: Board,
    ) {
        val taskListHashMap = HashMap<String, Any>()
        taskListHashMap[Constants.TASK_LIST] = board.taskList
        mFireStore.collection(Constants.BOARDS)
            .document(board.documentId)
            .update(taskListHashMap)
            .addOnSuccessListener {
                activity.addUpdateTaskListSuccess()
            }
            .addOnFailureListener { e ->
                activity.loadingDialog.dismissDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating a board.", e)
                Toast.makeText(
                    activity,
                    "Error when creating a board.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
    }

    fun getAssignMemberListDetials(
        activity: MembersActivity,
        assignedTo: ArrayList<String>,
    ) {
        mFireStore.collection(Constants.USERS)
            .whereIn(Constants.ID, assignedTo)
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName, document.documents.toString())
                Log.i(activity.javaClass.simpleName, document.documents.toString())
                val usersList: ArrayList<User> = ArrayList()
                for (i in document.documents) {
                    val user = i.toObject(User::class.java)!!
                    usersList.add(user)
                }
                activity.setupMembersList(usersList)
            }
            .addOnFailureListener { e ->
                activity.loadingDialog.dismissDialog()
                Log.e(activity.javaClass.simpleName, "Error writing document", e)
            }
    }
}
