package fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.chitchat.R

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Beginner Style: Inflate layout traditionally
        val view = inflater.inflate(R.layout.fragment_splash, container, false)

        Handler(Looper.getMainLooper()).postDelayed({
            // Beginner Style: Manual Fragment Transaction
            val loginFragment = LoginFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.auth_container, loginFragment)
            transaction.commit()
        }, 2500)

        return view
    }
}