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

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Beginner Style: Inflate traditionally
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        // Beginner Style: findViewById
        val etName = view.findViewById<EditText>(R.id.et_name)
        val etPhone = view.findViewById<EditText>(R.id.et_phone)
        val btnRegister = view.findViewById<Button>(R.id.btn_register)
        val tvLogin = view.findViewById<TextView>(R.id.tv_login)

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val phone = etPhone.text.toString().trim()

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (phone.length != 10) {
                Toast.makeText(requireContext(), "Enter a valid 10-digit number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Beginner Style: Manual Fragment Transaction
            val otpFragment = OTPFragment()
            val bundle = Bundle()
            bundle.putString("number", phone)
            bundle.putString("name", name)
            otpFragment.arguments = bundle

            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.auth_container, otpFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        tvLogin.setOnClickListener {
            // Beginner Style: Go back
            parentFragmentManager.popBackStack()
        }

        return view
    }
}