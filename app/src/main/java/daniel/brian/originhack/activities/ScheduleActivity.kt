package daniel.brian.originhack.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import daniel.brian.originhack.R
import daniel.brian.originhack.data.Appointment
import daniel.brian.originhack.databinding.ActivityScheduleBinding
import daniel.brian.originhack.utils.Resource
import daniel.brian.originhack.viewmodel.AppointmentsViewModel
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ScheduleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScheduleBinding
    private val viewModel by viewModels<AppointmentsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            scheduleButton.setOnClickListener {
                val meeting = meetingType.text.toString()
                val dateTime = dateTime.text.toString()
                val name = patientName.text.toString()


                val appointment = Appointment(meeting,dateTime,name)
                viewModel.getAppointment(appointment)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.appointment.collect {
                when(it){
                    is Resource.Error -> {
                        Toast.makeText(this@ScheduleActivity, it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        Toast.makeText(this@ScheduleActivity, "Loading...", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success -> {
                        Toast.makeText(this@ScheduleActivity, "Appointment Booked Successfully", Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.error.collectLatest {
                Toast.makeText(this@ScheduleActivity, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}