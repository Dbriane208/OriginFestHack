package daniel.brian.originhack

import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val generativeModel: GenerativeModel
) {
    suspend fun getAIResponse(prompt: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = generativeModel.generateContent(prompt)
            Result.success(response.text ?: "No response generated")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// ChatMessage.kt
data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

// ChatState.kt
sealed class ChatState {
    object Idle : ChatState()
    object Loading : ChatState()
    data class Success(val messages: List<ChatMessage>) : ChatState()
    data class Error(val message: String) : ChatState()
}