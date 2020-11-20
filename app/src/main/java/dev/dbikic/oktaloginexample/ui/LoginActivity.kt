package dev.dbikic.oktaloginexample.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.okta.oidc.*
import com.okta.oidc.util.AuthorizationException
import dev.dbikic.oktaloginexample.OktaLoginExampleApplication
import dev.dbikic.oktaloginexample.R
import dev.dbikic.oktaloginexample.extensions.showShortToast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupOktaCallback()
        setupViews()
    }

    private fun setupOktaCallback() {
        (application as OktaLoginExampleApplication).oktaManger.registerWebAuthCallback(getAuthCallback(), this)
    }

    private fun setupViews() {
        signInButton.setOnClickListener {
            val payload = AuthenticationPayload.Builder().build()
            (application as OktaLoginExampleApplication).oktaManger.signIn(this, payload)
        }
    }

    private fun getAuthCallback(): ResultCallback<AuthorizationStatus, AuthorizationException> {
        return object : ResultCallback<AuthorizationStatus, AuthorizationException> {
            override fun onSuccess(result: AuthorizationStatus) {
                when (result) {
                    AuthorizationStatus.AUTHORIZED -> {
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    }
                    AuthorizationStatus.SIGNED_OUT -> showShortToast("Signed out")
                    AuthorizationStatus.CANCELED -> showShortToast("Canceled")
                    AuthorizationStatus.ERROR -> showShortToast("Error")
                    AuthorizationStatus.EMAIL_VERIFICATION_AUTHENTICATED -> showShortToast("Email verification authenticated")
                    AuthorizationStatus.EMAIL_VERIFICATION_UNAUTHENTICATED -> showShortToast("Email verification unauthenticated")
                }
            }

            override fun onCancel() {
                showShortToast("Canceled")
            }

            override fun onError(msg: String?, exception: AuthorizationException?) {
                showShortToast("Error: $msg")
            }
        }
    }
}