package adapters

import Activities.ChatActivity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.Utils
import com.example.chitchat.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import models.Users

class SearchUserAdapter(options: FirebaseRecyclerOptions<Users>) :
    FirebaseRecyclerAdapter<Users, SearchUserAdapter.UserViewHolder>(options) {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val userNumber: TextView = itemView.findViewById(R.id.user_number)
        val userStatus: TextView = itemView.findViewById(R.id.user_status)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_user_row, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: UserViewHolder,
        position: Int,
        model: Users
    ) {
        holder.userNumber.text = "+91 " + model.phoneNumber
        holder.userStatus.text = model.status

        if (model.uid == Utils.getUserId()) {
            holder.userName.text = model.username + " (You)"
        } else {
            holder.userName.text = model.username
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ChatActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("UserName", model.username)
            intent.putExtra("UserId", model.uid)
            holder.itemView.context.startActivity(intent)
        }
    }
}
