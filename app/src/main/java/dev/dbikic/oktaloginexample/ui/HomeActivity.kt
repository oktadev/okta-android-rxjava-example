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
import dev.dbikic.oktaloginexample.ProfilesAdapter
import dev.dbikic.oktaloginexample.databinding.ActivityHomeBinding
import dev.dbikic.oktaloginexample.model.Profile
import dev.dbikic.oktaloginexample.model.ProfileRequest
import dev.dbikic.oktaloginexample.network.ProfileService
import dev.dbikic.oktaloginexample.network.RetrofitClientInstance
import dev.dbikic.oktaloginexample.network.RetrofitClientInstance.retrofitInstance
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeActivity : AppCompatActivity() {

    private val profileService: ProfileService = retrofitInstance.create(
        ProfileService::class.java
    )
    private var compositeDisposable = CompositeDisposable()

    private var adapter = ProfilesAdapter(
        onDeleteClickListener = { profile -> deleteProfile(profile) },
        onUpdateClickListener = { profile -> updateProfile(profile) }
    )

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

        binding.createProfileButton.setOnClickListener { createProfile() }
        binding.profilesRecyclerView.adapter = adapter
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
                RetrofitClientInstance.setToken(oktaManager.getJwtToken())
                fetchProfiles()
            }

            override fun onError(msg: String?, exception: AuthorizationException?) {
                Log.d("HomeActivity", "Error: $msg")
            }
        }
    }

    private fun fetchProfiles() {
        compositeDisposable.add(
            profileService.getProfiles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { profiles ->
                        displayProfiles(profiles)
                    },
                    { throwable ->
                        Log.e("HomeActivity", throwable.message ?: "onError")
                    }
                )
        )
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }

    private fun createProfile() {
        val profile = ProfileRequest(email = System.currentTimeMillis().toString())
        compositeDisposable.add(
            profileService.createProfile(profile)
                .andThen(profileService.getProfiles())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { profiles ->
                        displayProfiles(profiles)
                    },
                    { throwable ->
                        Log.e("HomeActivity", throwable.message ?: "onError")
                    }
                )
        )
    }

    private fun deleteProfile(profile: Profile) {
        compositeDisposable.add(
            profileService.deleteProfile(profile.id)
                .andThen(profileService.getProfiles())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { profiles ->
                        displayProfiles(profiles)
                    },
                    { throwable ->
                        Log.e("HomeActivity", throwable.message ?: "onError")
                    }
                )
        )
    }

    private fun updateProfile(oldProfile: Profile) {
        val profile = ProfileRequest(email = System.currentTimeMillis().toString())
        compositeDisposable.add(
            profileService.updateProfile(oldProfile.id, profile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { newProfiles ->
                        displayUpdatedProfile(oldProfile, newProfiles.first())
                    },
                    { throwable ->
                        Log.e("HomeActivity", throwable.message ?: "onError")
                    }
                )
        )
    }

    private fun displayProfiles(profiles: List<Profile>) {
        adapter.items.clear()
        adapter.items.addAll(profiles)
        adapter.notifyDataSetChanged()
    }

    private fun displayUpdatedProfile(oldProfile: Profile, newProfile: Profile) {
        val index = adapter.items.indexOfFirst { profileToReplace ->
            profileToReplace.email == oldProfile.email
        }
        adapter.items[index] = newProfile
        adapter.notifyItemChanged(index)
    }
}
