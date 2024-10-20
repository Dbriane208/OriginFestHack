package daniel.brian.originhack.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import daniel.brian.originhack.ChatAdapter
import daniel.brian.originhack.ChatState
import daniel.brian.originhack.R
import daniel.brian.originhack.databinding.ActivityAdviceBinding
import daniel.brian.originhack.utils.Resource
import daniel.brian.originhack.viewmodel.AdviceViewModel
import daniel.brian.originhack.viewmodel.AppointmentsViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdviceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdviceBinding
    private val viewModel: AdviceViewModel by viewModels()
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupClickListeners()
        observeState()
        observeMessages()

        // Show initial greeting
        viewModel.sendMessage("Hello, I'm here to help you with medical advice. What questions do you have?")
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter()
        binding.chatRecyclerView.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@AdviceActivity)
        }
    }

    private fun setupClickListeners() {
        binding.sendButton.setOnClickListener {
            val message = binding.messageInput.text.toString().trim()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(message)
                binding.messageInput.setText("")
            }
        }

        // Enable/disable send button based on input
        binding.messageInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                binding.sendButton.isEnabled = !s.isNullOrBlank()
            }
        })
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.chatState.collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        binding.loadingIndicator.isVisible = true
                        binding.sendButton.isEnabled = false
                    }
                    is Resource.Success -> {
                        binding.loadingIndicator.isVisible = false
                        binding.sendButton.isEnabled = true
                        scrollToBottom()
                    }
                    is Resource.Error -> {
                        binding.loadingIndicator.isVisible = false
                        binding.sendButton.isEnabled = true
                        Toast.makeText(this@AdviceActivity, state.message ?: "An error occurred", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Unspecified -> {
                        binding.loadingIndicator.isVisible = false
                        binding.sendButton.isEnabled = true
                    }
                }
            }
        }
    }


    private fun observeMessages() {
        lifecycleScope.launch {
            viewModel.messages.collect { messages ->
                chatAdapter.submitList(messages.toMutableList())
                scrollToBottom()
            }
        }
    }

    private fun scrollToBottom() {
        binding.chatRecyclerView.postDelayed({
            binding.chatRecyclerView.smoothScrollToPosition(chatAdapter.itemCount - 1)
        }, 100)
    }
}