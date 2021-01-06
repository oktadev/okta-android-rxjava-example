package dev.dbikic.oktaloginexample.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.okta.oidc.*
import com.okta.oidc.AuthorizationStatus.*
import com.okta.oidc.util.AuthorizationException
import dev.dbikic.oktaloginexample.OktaLoginApplication
import dev.dbikic.oktaloginexample.OktaManager
import dev.dbikic.oktaloginexample.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val oktaManager: OktaManager by lazy { (application as OktaLoginApplication).oktaManager }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupOktaCallback()
        setupViews()
    }

    private fun setupOktaCallback() {
        oktaManager.registerWebAuthCallback(getAuthCallback(), this)
    }

    private fun setupViews() {
        binding.signInButton.setOnClickListener {
            val payload = AuthenticationPayload.Builder().build()
            oktaManager.signIn(this, payload)
        }
    }

    private fun getAuthCallback(): ResultCallback<AuthorizationStatus, AuthorizationException> {
        return object : ResultCallback<AuthorizationStatus, AuthorizationException> {
            override fun onSuccess(result: AuthorizationStatus) {
                when (result) {
                    AUTHORIZED -> navigateToHome()
                    SIGNED_OUT -> Log.d("LoginActivity", "Signed out")
                    CANCELED -> Log.d("LoginActivity", "Canceled")
                    ERROR -> Log.d("LoginActivity", "Error")
                    EMAIL_VERIFICATION_AUTHENTICATED -> Log.d("LoginActivity", "Email verification authenticated")
                    EMAIL_VERIFICATION_UNAUTHENTICATED -> Log.d("LoginActivity", "Email verification unauthenticated")
                }
            }

            override fun onCancel() {
                Log.d("LoginActivity", "Canceled")
            }

            override fun onError(msg: String?, exception: AuthorizationException?) {
                Log.d("LoginActivity", "Error: $msg")
            }
        }
    }

    private fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}
