package mnxk.kotlintex.projectm.activities

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar.*
import com.google.firebase.auth.FirebaseAuth
import mnxk.kotlintex.projectm.R
import mnxk.kotlintex.projectm.databinding.ActivityBaseBinding
import mnxk.kotlintex.projectm.databinding.DialogCustomProgressBinding
import java.util.concurrent.Executors.newSingleThreadScheduledExecutor
import java.util.concurrent.TimeUnit.SECONDS

open class BaseActivity : AppCompatActivity() {
    // A double back press to exit the app
    private var doubleBackToExitPressedOnce = false

    private lateinit var mProgressDialog: Dialog
    private var binding: ActivityBaseBinding? = null

//    private var dialogBinding = DialogCustomProgressBinding.inflate(layoutInflater)
    private lateinit var dialogBinding: DialogCustomProgressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_base)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        dialogBinding = DialogCustomProgressBinding.inflate(layoutInflater)
    }

    fun showProgessDialog(text: String) {
        mProgressDialog = Dialog(this)
        mProgressDialog.setContentView(dialogBinding.root)
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        dialogBinding.tvMessage.text = text
        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    fun getCurrentUserId(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    private var executor = newSingleThreadScheduledExecutor()

    fun doubleBackToExit() {
        if (doubleBackToExitPressedOnce) {
            finish()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(
            this,
            getString(R.string.please_click_back_again_to_exit),
            Toast.LENGTH_SHORT,
        ).show()
//        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
//        executor.schedule({ doubleBackToExitPressedOnce = false }, 2000, MILLISECONDS)
        executor.schedule({ doubleBackToExitPressedOnce = false }, 2, SECONDS)
        hideProgressDialog()
    }

    fun showErrorSnackBar(message: String) {
        val snackBar =
            make(
                findViewById(android.R.id.content),
                message,
                LENGTH_LONG,
            )
        val snackbarView = snackBar.view
        snackbarView.setBackgroundColor(getColor(R.color.snackbar_error_background))
        snackBar.show()
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
