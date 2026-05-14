package Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.Utils
import com.example.chitchat.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase
import fragments.ChatFragment
import fragments.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Beginner Style: findViewById
        toolbar = findViewById(R.id.toolbar)
        bottomNav = findViewById(R.id.bottom_nav_view)

        updateStatus("Online")

        // Default fragment
        if (savedInstanceState == null) {
            showChatFragment()
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.chats -> {
                    showChatFragment()
                    true
                }
                R.id.profile -> {
                    showProfileFragment()
                    true
                }
                else -> false
            }
        }

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.search -> {
                    val intent = Intent(this, SearchUserActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        toolbar.setNavigationOnClickListener {
            showChatFragment()
            bottomNav.selectedItemId = R.id.chats
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

    private fun showChatFragment() {
        toolbar.title = "Chit Chat"
        toolbar.navigationIcon = null
        val searchItem = toolbar.menu.findItem(R.id.search)
        searchItem?.isVisible = true
        
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, ChatFragment())
        transaction.commit()
    }

    private fun showProfileFragment() {
        toolbar.title = "Profile"
        toolbar.setNavigationIcon(R.drawable.ic_back) 
        val searchItem = toolbar.menu.findItem(R.id.search)
        searchItem?.isVisible = false
        
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, ProfileFragment())
        transaction.commit()
    }

    override fun onResume() {
        super.onResume()
        updateStatus("Online")
    }
}