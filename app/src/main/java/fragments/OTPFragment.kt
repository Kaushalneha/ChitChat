package fragments

import Activities.MainActivity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.AuthViewMode
import com.Utils
import com.example.chitchat.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import models.Users

class OTPFragment : Fragment() {

    private val viewModel: AuthViewMode by viewModels()
    private lateinit var number: String
    private var name: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_o_t_p, container, false)

        // Beginner Style: findViewById
        val tvNumber = view.findViewById<TextView>(R.id.number)
        val etOtpLayout = view.findViewById<TextInputLayout>(R.id.et_otp)
        val btnContinue = view.findViewById<Button>(R.id.btn_continue)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)

        // Get data from Bundle
        val bundle = arguments
        if (bundle != null) {
            number = bundle.getString("number").toString()
            name = bundle.getString("name") // Name from RegisterFragment
            tvNumber.text = "+91 $number"
        }

        observeSignInState()
        viewModel.sendOTP(number, requireActivity())

        btnContinue.setOnClickListener {
            val otp = etOtpLayout.editText?.text.toString()
            if (otp.length == 6) {
                Toast.makeText(requireContext(), "Verifying...", Toast.LENGTH_SHORT).show()
                viewModel.signInWithPhoneAuthCredential(otp, requireActivity())
            } else {
                Toast.makeText(requireContext(), "Enter 6-digit OTP", Toast.LENGTH_SHORT).show()
            }
        }

        toolbar.setNavigationOnClickListener {
            // Manual Back Transaction
            val loginFragment = LoginFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.auth_container, loginFragment)
            transaction.commit()
        }

        return view
    }

    private fun observeSignInState() {
        lifecycleScope.launch {
            viewModel.isSignedIn.collect { success ->
                if (success == true) {
                    val uid = Utils.getUserId()
                    
                    if (!name.isNullOrEmpty()) {
                        // Beginner Style: Direct Save if name is present (From Register)
                        saveUserToFirebase(uid)
                    } else {
                        // If no name, go to UserDetails (From Login)
                        val userDetails = UserDetails()
                        val bundle = Bundle()
                        bundle.putString("number", number)
                        userDetails.arguments = bundle
                        
                        val transaction = parentFragmentManager.beginTransaction()
                        transaction.replace(R.id.auth_container, userDetails)
                        transaction.commit()
                    }
                    viewModel.resetIsSignedIn()
                } else if (success == false) {
                    Toast.makeText(requireContext(), "Verification Failed", Toast.LENGTH_SHORT).show()
                    viewModel.resetIsSignedIn()
                }
            }
        }
    }

    private fun saveUserToFirebase(uid: String) {
        val user = Users(uid, number, name ?: "")
        FirebaseDatabase.getInstance().getReference("AllUsers").child(uid)
            .setValue(user)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Welcome $name!", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to save user", Toast.LENGTH_SHORT).show()
            }
    }
}