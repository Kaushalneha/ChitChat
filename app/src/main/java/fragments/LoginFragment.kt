package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.chitchat.R

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Beginner Style: Inflate layout traditionally
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // Beginner Style: Use findViewById
        val etNumber = view.findViewById<EditText>(R.id.et_number)
        val btnContinue = view.findViewById<Button>(R.id.btn_continue)
        val tvRegister = view.findViewById<TextView>(R.id.tv_register)

        btnContinue.setOnClickListener {
            val number = etNumber.text.toString().trim()

            if (number.isEmpty() || number.length != 10) {
                Toast.makeText(requireContext(), "Enter a valid 10-digit number", Toast.LENGTH_SHORT).show()
            } else {
                // Beginner Style: Manual Fragment Transaction
                val otpFragment = OTPFragment()
                val bundle = Bundle()
                bundle.putString("number", number)
                otpFragment.arguments = bundle

                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.auth_container, otpFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }

        tvRegister.setOnClickListener {
            // Beginner Style: Manual Fragment Transaction
            val registerFragment = RegisterFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.auth_container, registerFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }
}