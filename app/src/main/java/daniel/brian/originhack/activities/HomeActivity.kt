package daniel.brian.originhack.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import daniel.brian.originhack.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    companion object {
        val OXYGEN = "daniel.brian.originhack.activities.oxygen"
        val BP = "daniel.brian.originhack.activities.bp"
        val ACTIVITY = "daniel.brian.originhack.activities.activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.advice.setOnClickListener {
            val intent = Intent(this,AdviceActivity::class.java)
            startActivity(intent)
        }

        binding.recommendation.setOnClickListener {
            val intent = Intent(this, RecommendationActivity::class.java)
            intent.putExtra(OXYGEN,binding.oxygen.text)
            intent.putExtra(BP,binding.bloodPressure.text)
            intent.putExtra(ACTIVITY,binding.steps.text)
            startActivity(intent)
        }

        binding.appointment.setOnClickListener {
            val intent = Intent(this, ScheduleActivity::class.java)
            startActivity(intent)
        }

//         function to collect data
        collectData()

    }

    private fun collectData() {
        val database = FirebaseDatabase.getInstance()
        val db = database.getReference("sensor")

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Check for expected structure (e.g., a map with keys matching properties)
                if (snapshot.value is Map<*, *>) {
                    val dataMap = snapshot.value as Map<*, *>
                    val heartRate = dataMap["heartRate"]?.toString() ?: "N/A"
                    val spo2 = dataMap["spo2"]?.toString() ?: "N/A"


                    binding.oxygen.text = spo2
                    binding.bloodPressure.text = heartRate

                } else {
                    // Handle unexpected structure (log error, display a message)
                    Toast.makeText(this@HomeActivity,"Unexpected data structure in Sensor",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HomeActivity,error.message,Toast.LENGTH_LONG).show()
            }

        })
    }
}