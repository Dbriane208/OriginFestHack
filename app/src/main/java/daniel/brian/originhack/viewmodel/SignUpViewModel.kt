package daniel.brian.originhack.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import daniel.brian.originhack.data.User
import daniel.brian.originhack.utils.RegisterFieldState
import daniel.brian.originhack.utils.RegisterValidation
import daniel.brian.originhack.utils.Resource
import daniel.brian.originhack.utils.validateEmail
import daniel.brian.originhack.utils.validatePassword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@Suppress("DEPRECATION")
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
): ViewModel() {

    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register = _register.asStateFlow()

    private val _validation = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()

    fun createAccountWithEmailAndPassword(user: User, password: String) {
        if (checkValidation(user, password)) {
            runBlocking {
                _register.emit(Resource.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener { it ->
                    it.user?.let {
                        saveUserInfo(it.uid, user)
                    }
                }.addOnFailureListener {
                    _register.value = Resource.Error(it.message.toString())
                }
        } else {
            val registerFieldState = RegisterFieldState(
                validateEmail(user.email),
                validatePassword(password)
            )

            // We're using send by the fact that we're using Channel to push the registerFieldState to the pull-based Flow
            runBlocking {
                _validation.send(registerFieldState)
            }
        }
    }

    private fun checkValidation(user: User, password: String): Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)

        return emailValidation is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success
    }

    private fun saveUserInfo(userUid: String, user: User) {
        db.collection("user")
            .document(userUid) // unique id of each customer
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }.addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
    }
}