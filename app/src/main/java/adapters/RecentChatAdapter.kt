package adapters

import Activities.ChatActivity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chitchat.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import models.RecentChatModel
import models.Users

class RecentChatAdapter(private val chatList: ArrayList<RecentChatModel>) :
    RecyclerView.Adapter<RecentChatAdapter.RecentChatViewHolder>() {

    inner class RecentChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val lastMessage: TextView = itemView.findViewById(R.id.user_number)
        val userStatus: TextView = itemView.findViewById(R.id.user_status)
        var statusListener: ValueEventListener? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_user_row, parent, false)
        return RecentChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecentChatViewHolder, position: Int) {
        val model = chatList[position]

        // Remove old listener if it exists to prevent multiple listeners on the same holder
        holder.statusListener?.let {
            FirebaseDatabase.getInstance().getReference("AllUsers").child(model.otherUserId)
                .removeEventListener(it)
        }

        // Attach a real-time listener for user details and status
        holder.statusListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(Users::class.java)
                if (user != null) {
                    holder.userName.text = user.username
                    holder.lastMessage.text = model.lastMessage
                    holder.userStatus.text = user.status
                    
                    holder.itemView.setOnClickListener {
                        val intent = Intent(holder.itemView.context, ChatActivity::class.java)
                        intent.putExtra("UserName", user.username)
                        intent.putExtra("UserId", user.uid)
                        holder.itemView.context.startActivity(intent)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        FirebaseDatabase.getInstance().getReference("AllUsers").child(model.otherUserId)
            .addValueEventListener(holder.statusListener!!)
    }

    override fun getItemCount(): Int = chatList.size
}