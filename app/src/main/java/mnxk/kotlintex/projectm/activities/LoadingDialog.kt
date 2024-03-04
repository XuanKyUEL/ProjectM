package mnxk.kotlintex.projectm.activities

import android.app.Activity
import android.widget.TextView
import mnxk.kotlintex.projectm.R

class LoadingDialog(
    private val activity: Activity,
) {
    private var dialog: android.app.AlertDialog? = null

    fun startLoadingDialog(message: String? = "Loading") {
        val builder = android.app.AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_progress_dialog, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        val textView = dialogView.findViewById<TextView>(R.id.tv_progress_text)
        message?.let {
            textView.text = it
        }
        dialog = builder.create()
        dialog?.show()
    }

    fun dismissDialog() {
        dialog?.dismiss()
    }
}
