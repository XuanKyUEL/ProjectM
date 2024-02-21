package mnxk.kotlintex.projectm.firebase


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import mnxk.kotlintex.projectm.activities.SignUpActivity
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
                activity.hideProgressDialog()
                activity.showErrorSnackBar(e.message.toString())
            }

        fun getCurrentUserId(): String {
            return FirebaseAuth.getInstance().currentUser!!.uid
        }

    }
}
