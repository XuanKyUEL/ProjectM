package mnxk.kotlintex.projectm.utils

import android.content.Intent
import android.os.Build
import android.os.Bundle
import java.io.Serializable

object Constants {
    val CARD_DETAIL: String = "cardDetail"
    val EMAIL: String = "email"
    val TASK_LIST: String = "taskList"
    val DOCUMENT_ID: String = "documentId"
    const val BOARD_IMAGE: String = "board_image"
    const val USERS: String = "users"
    const val PROJECTM_PREFERENCES: String = "ProjectMPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 101
    const val MY_PROFILE_REQUEST_CODE = 11
    const val IMAGE: String = "image"
    const val NAME: String = "name"
    const val MOBILE: String = "mobile"
    const val BOARDS = "boards"
    const val ASSIGNED_TO: String = "assignedTo"
    const val CREATE_BOARD_REQUEST_CODE = 13
    const val BOARD_DETAIL: String = "board_detail"
    const val ID: String = "id"
    const val TASK_LIST_ITEM_POSITION: String = "task_list_item_position"
    const val CARD_LIST_ITEM_POSITION: String = "card_list_item_position"
}

@Suppress("DEPRECATION")
object IntentUtils {
    inline fun <reified T : java.io.Serializable> Bundle.serializable(key: String): T? =
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
            else ->
                @Suppress("DEPRECATION")
                getSerializable(key)
                    as? T
        }

    inline fun <reified T : Serializable> Intent.serializable(key: String): T? =
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
            else ->
                @Suppress("DEPRECATION")
                getSerializableExtra(key)
                    as? T
        }

    inline fun <reified Board> getParcelableExtra(
        intent: Intent?,
        s: String,
    ): Board? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(s, Board::class.java)
        } else {
            intent?.getParcelableExtra(s)
        }
    }
}
