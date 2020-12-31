package dev.dbikic.oktaloginexample.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.okta.oidc.RequestCallback
import com.okta.oidc.net.response.UserInfo
import com.okta.oidc.util.AuthorizationException
import dev.dbikic.oktaloginexample.OktaLoginApplication
import dev.dbikic.oktaloginexample.OktaManager
import dev.dbikic.oktaloginexample.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private val oktaManager: OktaManager by lazy { (application as OktaLoginApplication).oktaManager }
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        oktaManager.registerUserProfileCallback(getUserProfileCallback())
        binding.signOutButton.setOnClickListener {
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
                Log.d("HomeActivity", "Error: $msg")
            }
        }
    }

    private fun getUserProfileCallback(): RequestCallback<UserInfo, AuthorizationException> {
        return object : RequestCallback<UserInfo, AuthorizationException> {
            override fun onSuccess(result: UserInfo) {
                binding.userLabel.text = "Hello, ${result["preferred_username"]}!"
            }

            override fun onError(msg: String?, exception: AuthorizationException?) {
                Log.d("HomeActivity", "Error: $msg")
            }
        }
    }
}
