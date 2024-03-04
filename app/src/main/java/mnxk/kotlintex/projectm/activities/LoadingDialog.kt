package mnxk.kotlintex.projectm.activities

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.TextView
import mnxk.kotlintex.projectm.R

class LoadingDialog(
    private val activity: Activity,
) {
    private var dialog: android.app.Dialog? = null

    fun startLoadingDialog(message: String? = "Loading") {
        dialog =
            Dialog(activity).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setContentView(R.layout.custom_progress_dialog)
                setCancelable(false)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }

        val textView = dialog?.findViewById<TextView>(R.id.tv_progress_text)
        message?.let {
            textView?.text = it
        }

        dialog?.show()
    }

    fun dismissDialog() {
        dialog?.dismiss()
    }
}
