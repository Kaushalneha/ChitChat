package Activities

import adapters.SearchUserAdapter
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.Utils
import com.example.chitchat.R
import com.example.chitchat.databinding.ActivitySearchUserBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import models.Users

class SearchUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchUserBinding
    private lateinit var adapter: SearchUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.searchBtn.setOnClickListener {
            val searchTxt = binding.etSearch.text.toString().trim()
            if (searchTxt.isEmpty()) {
                binding.etSearch.error = "Please enter a name"
            } else {
                setupSearchRecyclerView(searchTxt)
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

    private fun setupSearchRecyclerView(searchTxt: String) {
        val query = FirebaseDatabase.getInstance().getReference("AllUsers")
            .orderByChild("username")
            .startAt(searchTxt)
            .endAt(searchTxt + "\uf8ff")


        val option = FirebaseRecyclerOptions.Builder<Users>()
            .setQuery(query, Users::class.java)
            .build()

        adapter = SearchUserAdapter(option)
        binding.userList.adapter = adapter
        binding.userList.layoutManager = LinearLayoutManager(this)
        
        adapter.startListening()
    }

    override fun onStart() {
        super.onStart()
        if (::adapter.isInitialized) {
            adapter.startListening()
        }
    }

    override fun onStop() {
        super.onStop()
        if (::adapter.isInitialized) {
            adapter.stopListening()
        }
    }
}
