package adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.Utils
import com.example.chitchat.R
import com.google.firebase.database.FirebaseDatabase
import models.MessageModel
import models.Users

// Beginner Style: Standard RecyclerView.Adapter instead of FirebaseRecyclerAdapter
class ChatAdapter(private val messageList: ArrayList<MessageModel>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private val VIEW_TYPE_SENT = 1
    private val VIEW_TYPE_RECEIVED = 2

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].senderId == Utils.getUserId()) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val layout = if (viewType == VIEW_TYPE_SENT) {
            R.layout.chat_right
        } else {
            R.layout.chat_left
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val model = messageList[position]
        holder.messageText.text = model.message

        // Beginner Style: Manual Firebase call to get sender name
        FirebaseDatabase.getInstance().getReference("AllUsers").child(model.senderId)
            .get().addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(Users::class.java)
                holder.senderName.text = user?.username ?: "Unknown"
            }
    }

    override fun getItemCount(): Int = messageList.size
    private fun startListening() {
        TODO("Not yet implemented")
    }

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.chat_message)
        val senderName: TextView = itemView.findViewById(R.id.sender_name)
    }
}