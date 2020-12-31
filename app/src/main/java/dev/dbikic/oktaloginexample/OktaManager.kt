package dev.dbikic.oktaloginexample

import android.app.Activity
import android.content.Context
import com.okta.oidc.*
import com.okta.oidc.clients.sessions.SessionClient
import com.okta.oidc.clients.web.WebAuthClient
import com.okta.oidc.net.response.UserInfo
import com.okta.oidc.storage.security.DefaultEncryptionManager
import com.okta.oidc.util.AuthorizationException

class OktaManager(applicationContext: Context) {

    /**
     * Authorization client using chrome custom tab as a user agent.
     */
    private var webAuth: WebAuthClient // <1>

    /**
     * The authorized client to interact with Okta's endpoints.
     */
    private var sessionClient: SessionClient // <2>

    init {
        val config = OIDCConfig.Builder()
            .clientId("0oa39f9k5cre3IqpN5d6") // <3>
            .discoveryUri("https://dev-6974382.okta.com") // <4>
            .redirectUri("com.okta.dev-6974382:/callback") // <5>
            .endSessionRedirectUri("com.okta.dev-6974382:/") // <6>
            .scopes("openid", "profile", "offline_access")
            .create()
        webAuth = Okta.WebAuthBuilder()
            .withConfig(config)
            .withContext(applicationContext)
            .withCallbackExecutor(null)
            .withEncryptionManager(DefaultEncryptionManager(applicationContext))
            .setRequireHardwareBackedKeyStore(false)  // <7>
            .create()
        sessionClient = webAuth.sessionClient
    }

    fun isAuthenticated(): Boolean {
        return sessionClient.isAuthenticated
    }

    fun registerWebAuthCallback(callback: ResultCallback<AuthorizationStatus, AuthorizationException>, activity: Activity) {
        webAuth.registerCallback(callback, activity)
    }

    fun registerUserProfileCallback(callback: RequestCallback<UserInfo, AuthorizationException>) {
        sessionClient.getUserProfile(callback)
    }

    fun signIn(activity: Activity, payload: AuthenticationPayload) {
        webAuth.signIn(activity, payload)
    }

    fun signOut(activity: Activity, callback: RequestCallback<Int, AuthorizationException>) {
        webAuth.signOut(activity, callback)
    }

    fun clearUserData() {
        sessionClient.clear()
    }
}
