package dev.dbikic.oktaloginexample.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.okta.oidc.RequestCallback
import com.okta.oidc.net.response.UserInfo
import com.okta.oidc.util.AuthorizationException
import dev.dbikic.oktaloginexample.OktaLoginApplication
import dev.dbikic.oktaloginexample.R
import dev.dbikic.oktaloginexample.extensions.showShortToast
import dev.dbikic.oktaloginexample.managers.OktaManager
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(R.layout.activity_home) {

    private val oktaManager: OktaManager by lazy { (application as OktaLoginApplication).oktaManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        oktaManager.registerUserProfileCallback(getUserProfileCallback())
        signOutButton.setOnClickListener {
            oktaManager.signOut(this, getSignOutCallback())
        }
    }

    private fun getSignOutCallback(): RequestCallback<Int, AuthorizationException> {
        return object : RequestCallback<Int, AuthorizationException> {
            override fun onSuccess(result: Int) {
                oktaManager.clearUserData()
                val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }

            override fun onError(msg: String?, exception: AuthorizationException?) {
                showShortToast("Error: $msg")
            }
        }
    }

    private fun getUserProfileCallback(): RequestCallback<UserInfo, AuthorizationException> {
        return object : RequestCallback<UserInfo, AuthorizationException> {
            override fun onSuccess(result: UserInfo) {
                userLabel.text = "Hello, ${result["preferred_username"]}!"
            }

            override fun onError(msg: String?, exception: AuthorizationException?) {
                showShortToast("Error: $msg")
            }
        }
    }
}