package daniel.brian.originhack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import daniel.brian.originhack.ChatRepository
import daniel.brian.originhack.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdviceViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _chatState = MutableStateFlow<Resource<ChatMessage>>(Resource.Unspecified())
    val chatState = _chatState.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    fun sendMessage(content: String) {
        viewModelScope.launch {
            try {
                _chatState.value =  Resource.Loading()

                // Add user message to the list
                val userMessage = ChatMessage(
                    content = content,
                    isFromUser = true,
                    timestamp = System.currentTimeMillis()
                )
                _messages.value = _messages.value + userMessage

                // Get response from repository
                val response = chatRepository.getAIResponse(content)

                // Add bot response to the list
                val botMessage = ChatMessage(
                    content = response.toString(),
                    isFromUser = false,
                    timestamp = System.currentTimeMillis()
                )
                _messages.value = _messages.value + botMessage

                _chatState.value = Resource.Success(null)
            } catch (e: Exception) {
                _chatState.value = Resource.Error(e.message ?: "An error occurred")
            }
        }
    }
}

data class ChatMessage(
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Long
)