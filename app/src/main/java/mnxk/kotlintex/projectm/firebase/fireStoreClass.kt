package mnxk.kotlintex.projectm.firebase

import com.google.firebase.firestore.FirebaseFirestore
import mnxk.kotlintex.projectm.activities.SignUpActivity
import mnxk.kotlintex.projectm.models.User

class fireStoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(
        activity: SignUpActivity,
        userInfo: User,
    ) {
    }
}
