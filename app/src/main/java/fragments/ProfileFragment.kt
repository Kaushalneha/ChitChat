package fragments

import Activities.AuthActivity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.Utils
import com.example.chitchat.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import models.Users

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        
        loadUserDetails()

        binding.btnLogout.setOnClickListener {
            logoutUser()
        }

        return binding.root
    }

    private fun loadUserDetails() {
        val uid = Utils.getUserId()
        if (uid.isNotEmpty()) {
            FirebaseDatabase.getInstance().getReference("AllUsers").child(uid)
                .get().addOnSuccessListener { snapshot ->
                    val user = snapshot.getValue(Users::class.java)
                    if (user != null) {
                        binding.profileName.text = user.username
                        binding.profileNumber.text = "+91 ${user.phoneNumber}"
                    }
                }
        }
    }

    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(requireContext(), AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}