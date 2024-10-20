package daniel.brian.originhack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import daniel.brian.originhack.data.Appointment
import daniel.brian.originhack.utils.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): ViewModel() {

    private val _appointment = MutableStateFlow<Resource<Appointment>>(Resource.Unspecified())
    val appointment = _appointment.asStateFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    fun getAppointment(appointment: Appointment) {
        val validateInputs = validateInputs(appointment)
        if(validateInputs){
            viewModelScope.launch {_appointment.emit(Resource.Loading())}

            firestore.collection("user").document(auth.uid!!).collection("appointments").document()
                .set(appointment).addOnSuccessListener {
                    viewModelScope.launch {_appointment.emit(Resource.Success(appointment))}
                }.addOnFailureListener {
                    viewModelScope.launch {_appointment.emit(Resource.Error(it.message.toString()))}
                }
        }else{
            viewModelScope.launch {_error.emit("Please fill all fields")}
        }
    }

    private fun validateInputs(appointment: Appointment): Boolean {
        return appointment.meeting.trim().isNotEmpty() &&
                appointment.dateTime.trim().isNotEmpty() &&
                appointment.name.trim().isNotEmpty()

    }
}