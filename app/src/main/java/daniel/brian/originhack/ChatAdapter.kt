package daniel.brian.originhack

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import daniel.brian.originhack.data.ChatMessage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {
    private val messages = mutableListOf<ChatMessage>()

    fun addMessage(message: ChatMessage) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount() = messages.size
    fun submitList(toMutableList: Any) {

    }

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val messageCard: MaterialCardView = view.findViewById(R.id.messageCard)
        private val messageText: TextView = view.findViewById(R.id.messageText)
        private val timestamp: TextView = view.findViewById(R.id.timestamp)

        fun bind(message: ChatMessage) {
            messageText.text = message.text
            timestamp.text = formatTimestamp(message.timestamp)

            // Style based on message sender
            if (message.isUser) {
                messageCard.setCardBackgroundColor(Color.parseColor("#E3F2FD"))
                (messageCard.layoutParams as ConstraintLayout.LayoutParams).apply {
                    horizontalBias = 1f
                    marginEnd = 0
                    marginStart = 48.dpToPx(itemView.context)
                }
            } else {
                messageCard.setCardBackgroundColor(Color.WHITE)
                (messageCard.layoutParams as ConstraintLayout.LayoutParams).apply {
                    horizontalBias = 0f
                    marginEnd = 48.dpToPx(itemView.context)
                    marginStart = 0
                }
            }
        }

        private fun formatTimestamp(timestamp: Long): String {
            return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(timestamp))
        }

        private fun Int.dpToPx(context: Context): Int {
            return (this * context.resources.displayMetrics.density).toInt()
        }
    }
}