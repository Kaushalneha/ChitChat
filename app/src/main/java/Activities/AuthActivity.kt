package Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chitchat.R
import fragments.SplashFragment

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // Load SplashFragment manually at start
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.auth_container, SplashFragment())
        transaction.commit()
    }
}