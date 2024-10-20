package daniel.brian.originhack.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
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

        // function to collect data
//        collectData()

    }

//    private fun collectData() {
//        val database = FirebaseDatabase.getInstance()
//        val db = database.getReference("Sensor")
//
//        db.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                // Check for expected structure (e.g., a map with keys matching properties)
//                if (snapshot.value is Map<*, *>) {
//                    val dataMap = snapshot.value as Map<*, *>
//                    val no2 = dataMap["NO2"]?.toString() ?: "N/A"
//                    val co = dataMap["CO"]?.toString() ?: "N/A"
//                    val ethylAlcohol = dataMap["ETHYL_ALCOHOL"]?.toString() ?: "N/A"
//                    val voc = dataMap["VOC"]?.toString() ?: "N/A"
//
//                    binding.nitrogen.text = no2
//                    binding.carbon.text = co
//                    binding.ethyl.text = ethylAlcohol
//                    binding.voc.text = voc
//
//                    if (co > 400.toString()){
//                        binding.overallValue.text = "High levels of CO detected"
//                        binding.overallValue.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.holo_red_dark))
//                    }else{
//                        binding.overallValue.text = "Moderate CO levels"
//                    }
//
//                } else {
//                    // Handle unexpected structure (log error, display a message)
//                    Toast.makeText(this@HomeActivity,"Unexpected data structure in Sensor",Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(this@HomeActivity,error.message,Toast.LENGTH_LONG).show()
//            }
//
//        })
//    }
}