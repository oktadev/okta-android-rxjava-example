package dev.dbikic.oktaloginexample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.dbikic.oktaloginexample.ui.HomeActivity
import dev.dbikic.oktaloginexample.ui.LoginActivity

class SplashActivity : AppCompatActivity() {

    private val oktaManager: OktaManager by lazy { (application as OktaLoginApplication).oktaManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (oktaManager.isAuthenticated()) {
            navigateToHome()
        } else {
            navigateToLogin()
        }
    }

    private fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
