package daniel.brian.originhack.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.ai.client.generativeai.GenerativeModel
import daniel.brian.originhack.databinding.ActivityRecommendationBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecommendationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecommendationBinding
    private var isLoading = false
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = "YOUR_API_KEY"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the sensor data
        val oxygen = intent.getStringExtra(HomeActivity.OXYGEN)
        val bp = intent.getStringExtra(HomeActivity.BP)
        val steps = intent.getStringExtra(HomeActivity.ACTIVITY)

        // Create the AI prompt
        val prompt = """
    
    Do not say something like This is a good start to an AI advice assistant for diabetes management, but it needs some improvements.
    
    say this sentence every time you return a response "Hello, I am your AI advice assistant from Care Sync, here to help you with your health based on the vitals we've collected"
    
    also your response should not be like it should recommend rather it should the response should be like...This is what I recommend you do

    The following data is from a patient suffering from diabetes:
    - Blood Pressure: $bp mmHg (target is 132 mmHg)
    - Oxygen Level: $oxygen% (target is 97%)
    - Activity: $steps steps taken today.
    
    Based on these values, I would encourage you to take the following steps to improve your vitals:
    
    1. BLOOD PRESSURE: If your blood pressure is higher or lower than the target, consider reducing salt intake, avoiding processed foods, and incorporating foods rich in potassium, such as spinach and bananas. Avoid stress and ensure you are getting enough rest.
    
    2. OXYGEN LEVEL: For maintaining healthy oxygen levels, practice deep breathing exercises and stay physically active. Make sure you are not sitting for long periods and consider light cardio exercises such as brisk walking.

    3. PHYSICAL ACTIVITY: You’ve taken $steps steps today. Aim for more consistent activity if needed. Moderate exercises like walking, cycling, or swimming can be beneficial. Increase your steps gradually over time to support better health outcomes.
    
    Don't say something like Contnact you doctor in the recommendations. I want the recommendation to be positive eg You should try to drink more water to raise oxygen levels , try to take more steps
    And then at the conclusion you can summarize and give the recommendions of the food to be in the diet and  then advice for regular checks up from medical doctors.
   
    In terms of diet, I recommend the following foods:
    - Whole grains such as oats, brown rice, or whole wheat.
    - Leafy green vegetables like kale and spinach.
    - Healthy fats from sources like avocados and nuts.
    
    Stay consistent with your workouts and manage your stress levels for better health outcomes.
""".trimIndent()


        // Handle button click to get AI recommendation
        binding.btnRec.setOnClickListener {
            if (!isLoading) {
                showLoadingState()
                lifecycleScope.launch {
                    try{
                        val recommendation = generateStory(prompt)
                        binding.recommendationTextView.text = recommendation
                    } catch (e: Exception) {
                        Toast.makeText(this@RecommendationActivity,"High traffic on the providers Model. Try Again later.",Toast.LENGTH_SHORT).show()
                    } finally {
                       hideLoadingState()
                    }
                }
            }
        }
    }


    private suspend fun generateStory(prompt: String): String {
        return withContext(Dispatchers.IO) {
            val response = generativeModel.generateContent(prompt)
            response.text.toString()
        }
    }

    private fun showLoadingState() {
        isLoading = true
        binding.apply {
            btnRec.isEnabled = false
            btnRec.alpha = 0.5f
            Toast.makeText(this@RecommendationActivity,"Loading....",Toast.LENGTH_SHORT).show()
            btnRec.text = "Generating..."
            recommendationTextView.text = ""
        }
    }

    private fun hideLoadingState() {
        isLoading = false
        binding.apply {
            btnRec.isEnabled = true
            btnRec.alpha = 1.0f
            btnRec.text = "Get Recommendation"
        }
    }

}