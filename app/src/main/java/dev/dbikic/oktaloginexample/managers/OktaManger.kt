package dev.dbikic.oktaloginexample.managers

import android.app.Activity
import android.content.Context
import com.okta.oidc.*
import com.okta.oidc.clients.sessions.SessionClient
import com.okta.oidc.clients.web.WebAuthClient
import com.okta.oidc.net.response.UserInfo
import com.okta.oidc.storage.security.DefaultEncryptionManager
import com.okta.oidc.util.AuthorizationException

class OktaManger(applicationContext: Context) {

    /**
     * Authorization client using chrome custom tab as a user agent.
     */
    private var webAuth: WebAuthClient

    /**
     * The authorized client to interact with Okta's endpoints.
     */
    private var sessionClient: SessionClient

    init {
        val config = OIDCConfig.Builder()
            .clientId("0oa10o2g0ZkB4lTNG5d6")
            .discoveryUri("https://dev-3143300.okta.com")
            .redirectUri("com.okta.dev-3143300:/callback")
            .endSessionRedirectUri("com.okta.dev-3143300:/logout")
            .scopes("openid", "profile", "offline_access")
            .create()
        webAuth = Okta.WebAuthBuilder()
            .withConfig(config)
            .withContext(applicationContext)
            .withCallbackExecutor(null)
            .withEncryptionManager(DefaultEncryptionManager(applicationContext))
            .setRequireHardwareBackedKeyStore(false)
            .create()
        sessionClient = webAuth.sessionClient
    }

    fun isAuthenticated(): Boolean {
        return sessionClient.isAuthenticated
    }

    fun registerWebAuthCallback(callback: ResultCallback<AuthorizationStatus, AuthorizationException>, activity: Activity) {
        webAuth.registerCallback(callback, activity)
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

    fun registerUserProfileCallback(callback: RequestCallback<UserInfo, AuthorizationException>) {
        sessionClient.getUserProfile(callback)
    }
}