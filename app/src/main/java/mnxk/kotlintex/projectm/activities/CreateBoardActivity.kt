package mnxk.kotlintex.projectm.activities

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import mnxk.kotlintex.projectm.R
import mnxk.kotlintex.projectm.databinding.ActivityCreateBoardBinding
import mnxk.kotlintex.projectm.firebase.fireStoreClass
import mnxk.kotlintex.projectm.models.Board
import mnxk.kotlintex.projectm.utils.Constants

class CreateBoardActivity : BaseActivity() {
    private lateinit var binding: ActivityCreateBoardBinding
    private lateinit var userName: String
    private var boardImageUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (intent.hasExtra(Constants.NAME)) {
            userName = intent.getStringExtra(Constants.NAME)!!
        }
        setupActionBar()
        mainEvents()
    }

    override fun onImagePicked() {
        try {
            binding.civBoardImage.setImageURI(imageUri)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.run {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
                title = resources.getString(R.string.create_board_title)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun mainEvents() {
        binding.civBoardImage.setOnClickListener {
            openImageChooser()
        }
        binding.btnCreate.setOnClickListener {
            if (validateBoardDetails()) {
                if (imageUri != null) {
                    uploadBoardImage()
                } else {
                    showProgessDialog("Creating board...")
                    createBoard()
                }
            }
        }
    }

    private fun createBoard() {
        val assignedUsersArrayList: ArrayList<String> = ArrayList()
        assignedUsersArrayList.add(getCurrentUserId())
        val board =
            Board(
                binding.etBoardName.text.toString(),
                boardImageUrl.toString(),
                userName,
                assignedUsersArrayList,
            )
        fireStoreClass().createBoard(this, board)
    }

    private fun uploadBoardImage() {
        showProgessDialog("Uploading board image...")
        if (imageUri != null) {
            val sRef: StorageReference =
                FirebaseStorage.getInstance().reference.child(
                    "BOARD_IMAGE" + System.currentTimeMillis() + "." + getFileExtension(this, imageUri),
                )

            sRef.putFile(imageUri!!).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { uri ->
                            Log.e("Downloadable Board Image URL", uri.toString())
                            boardImageUrl = uri.toString()
                            createBoard()
                        }
                        .addOnFailureListener { exception ->
                            Log.e("Firebase Storage", "Failed to get download URL: ${exception.message}")
                        }
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                    Log.e("Image Upload Error", task.exception?.message, task.exception)
                    hideProgressDialog()
                }
            }
        }
    }

    fun validateBoardDetails(): Boolean {
        return when {
            binding.etBoardName.text.toString().trim { it <= ' ' }.isEmpty() -> {
                showErrorSnackBar("Please enter a name for the board.")
                false
            }
            else -> {
                true
            }
        }
    }

    fun boardCreatedSuccessfully() {
        hideProgressDialog()
        Toast.makeText(this, "Board created successfully", Toast.LENGTH_SHORT).show()
        setResult(Activity.RESULT_OK)
        finish()
    }
}
