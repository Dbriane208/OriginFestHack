package daniel.brian.originhack.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import daniel.brian.originhack.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    firebaseAuth: FirebaseAuth
): ViewModel() {
    private val _navigate = MutableStateFlow(0)
    val navigate = _navigate.asStateFlow()

    companion object{
        const val SHOPPING_ACTIVITY = 1
        const val SPLASH_ACTIVITY = 2
    }

    // init blocks executed when instance of claas is created
    init{
        val isButtonClicked = sharedPreferences.getBoolean(Constants.INTRODUCTION_KEY, false)
        val user = firebaseAuth.currentUser

        if(user != null){
            viewModelScope.launch {
                _navigate.value = SHOPPING_ACTIVITY
            }
        }else if(isButtonClicked){
            viewModelScope.launch {
                _navigate.value = SPLASH_ACTIVITY
            }
        }
        else{
            Unit
        }
    }

    fun startButtonClick(){
        sharedPreferences.edit().putBoolean(Constants.INTRODUCTION_KEY, true).apply()
    }
}