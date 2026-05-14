package fragments

import adapters.RecentChatAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Utils
import com.example.chitchat.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import models.RecentChatModel

class ChatFragment : Fragment() {

    private lateinit var adapter: RecentChatAdapter
    private lateinit var chatList: ArrayList<RecentChatModel>
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Beginner Style: Traditional Inflation
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        // Beginner Style: findViewById
        recyclerView = view.findViewById(R.id.chat_recyclerView)
        
        chatList = ArrayList()
        adapter = RecentChatAdapter(chatList)
        
        recyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
            reverseLayout = true
            stackFromEnd = true
        }
        recyclerView.adapter = adapter

        setupRecentChatsListener()

        return view
    }

    private fun setupRecentChatsListener() {
        val currentUserId = Utils.getUserId()
        if (currentUserId.isEmpty()) return

        // Beginner Style: Manual ValueEventListener instead of FirebaseUI
        FirebaseDatabase.getInstance().getReference("RecentChats")
            .child(currentUserId)
            .orderByChild("lastMessageTimestamp")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    chatList.clear()
                    for (data in snapshot.children) {
                        val chat = data.getValue(RecentChatModel::class.java)
                        if (chat != null) {
                            chatList.add(chat)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}