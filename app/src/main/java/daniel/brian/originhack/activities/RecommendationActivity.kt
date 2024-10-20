package daniel.brian.originhack.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.ai.client.generativeai.GenerativeModel
import daniel.brian.originhack.R
import daniel.brian.originhack.databinding.ActivityRecommendationBinding

class RecommendationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecommendationBinding

    val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-pro",
        apiKey = ""
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendation)
    }
}