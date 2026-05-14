package Activities

import adapters.ChatAdapter
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Utils
import com.example.chitchat.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import models.ChatRoomModel
import models.MessageModel
import models.RecentChatModel
import models.Users

class ChatActivity : AppCompatActivity() {

    private var chatRoomId: String = ""
    private var user2Id: String = ""
    private lateinit var adapter: ChatAdapter
    private lateinit var messageList: ArrayList<MessageModel>

    private lateinit var recyclerView: RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var btnSend: ImageView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        recyclerView = findViewById(R.id.messageList)
        etMessage = findViewById(R.id.message)
        btnSend = findViewById(R.id.sentBtn)
        toolbar = findViewById(R.id.toolbar)


        user2Id = intent.getStringExtra("UserId") ?: ""
        val otherUserName = intent.getStringExtra("UserName") ?: "Chat"


        toolbar.title = otherUserName
        toolbar.setNavigationOnClickListener {
            finish()
        }


        messageList = ArrayList()
        adapter = ChatAdapter(messageList)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter


        observeUserStatus()


        if (user2Id.isNotEmpty()) {
            getOrCreateChatRoom()
        }


        btnSend.setOnClickListener {
            val messageText = etMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
            } else {
                Toast.makeText(this, "Please type a message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateStatus(status: String) {
        val uid = Utils.getUserId()
        if (uid.isNotEmpty()) {
            val database = FirebaseDatabase.getInstance().getReference("AllUsers").child(uid)
            database.child("status").setValue(status)
            database.child("status").onDisconnect().setValue("Offline")
        }
    }

    override fun onResume() {
        super.onResume()
        updateStatus("Online")
    }

    override fun onPause() {
        super.onPause()
        // Optional: You can set to "Offline" here, but usually keeping it "Online" 
        // until onDisconnect is better for brief app switches.
    }

    private fun observeUserStatus() {
        if (user2Id.isEmpty()) return
        FirebaseDatabase.getInstance().getReference("AllUsers").child(user2Id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(Users::class.java)
                    if (user != null) {
                        toolbar.subtitle = user.status
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }


    private fun setupMessageListener() {
        if (chatRoomId.isEmpty()) return

        FirebaseDatabase.getInstance().getReference("ChatRooms")
            .child(chatRoomId).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (data in snapshot.children) {
                        val message = data.getValue(MessageModel::class.java)
                        if (message != null) {
                            messageList.add(message)
                        }
                    }
                    adapter.notifyDataSetChanged()
                    if (messageList.size > 0) {
                        recyclerView.scrollToPosition(messageList.size - 1)
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun sendMessage(messageText: String) {
        val currentTime = System.currentTimeMillis()
        val currentUserId = Utils.getUserId()

        val messageModel = MessageModel(messageText, currentUserId, currentTime)


        FirebaseDatabase.getInstance().getReference("ChatRooms")
            .child(chatRoomId).child("messages").push().setValue(messageModel)


        val senderRecentChat = RecentChatModel(user2Id, messageText, currentTime, chatRoomId)
        FirebaseDatabase.getInstance().getReference("RecentChats")
            .child(currentUserId).child(user2Id).setValue(senderRecentChat)

        val receiverRecentChat = RecentChatModel(currentUserId, messageText, currentTime, chatRoomId)
        FirebaseDatabase.getInstance().getReference("RecentChats")
            .child(user2Id).child(currentUserId).setValue(receiverRecentChat)

        etMessage.setText("")
    }


    private fun getOrCreateChatRoom() {
        chatRoomId = getChatRoomId(Utils.getUserId(), user2Id)
        val chatRoomRef = FirebaseDatabase.getInstance().getReference("ChatRooms").child(chatRoomId)

        chatRoomRef.get().addOnSuccessListener { snapshot ->
            if (!snapshot.exists()) {
                val userIds = arrayListOf(Utils.getUserId(), user2Id)
                val chatRoom = ChatRoomModel(chatRoomId, userIds, System.currentTimeMillis(), "")
                chatRoomRef.setValue(chatRoom)
            }
            setupMessageListener()
        }
    }


    private fun getChatRoomId(userId1: String, userId2: String): String {
        return if (userId1.hashCode() < userId2.hashCode()) {
            userId1 + "_" + userId2
        } else {
            userId2 + "_" + userId1
        }
    }
}
