package fragments

import Activities.MainActivity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.Utils
import com.example.chitchat.databinding.FragmentUserDetailsBinding
import com.google.firebase.database.FirebaseDatabase
import models.Users

class UserDetails : Fragment() {

    private lateinit var binding: FragmentUserDetailsBinding
    var username = ""
    var userNumber = ""
    var uid = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailsBinding.inflate(layoutInflater)


        setupData()


        onContinueButtonClick()

        return binding.root
    }

    private fun setupData() {
        val bundle = arguments
        if (bundle != null) {
            userNumber = bundle.getString("number").toString()
        }
        uid = Utils.getUserId()
    }

    private fun onContinueButtonClick() {
        binding.btnContinue.setOnClickListener {
            username = binding.etUsername.text.toString().trim()

            if (username.isEmpty()) {
                binding.etUsername.error = "User name cannot be empty"
            } else {
                saveUserToDatabase()
            }
        }
    }

    private fun saveUserToDatabase() {
        val user = Users(uid, userNumber, username)


        FirebaseDatabase.getInstance().getReference("AllUsers")
            .child(uid)
            .setValue(user)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Profile Saved!", Toast.LENGTH_SHORT).show()


                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finishAffinity()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}