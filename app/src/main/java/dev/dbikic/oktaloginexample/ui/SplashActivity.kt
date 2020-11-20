package dev.dbikic.oktaloginexample.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.dbikic.oktaloginexample.OktaLoginExampleApplication

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkIfUserIsAuthenticated()
    }

    private fun checkIfUserIsAuthenticated() {
        val isAuthenticated = (application as OktaLoginExampleApplication).oktaManger.isAuthenticated()
        val intent = if (isAuthenticated) {
            Intent(this, HomeActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}