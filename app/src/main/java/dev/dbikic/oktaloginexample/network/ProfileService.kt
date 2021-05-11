package dev.dbikic.oktaloginexample.network

import dev.dbikic.oktaloginexample.model.Profile
import dev.dbikic.oktaloginexample.model.ProfileRequest
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface ProfileService {

    @GET("/profiles")
    fun getProfiles(): Observable<List<Profile>>

    @POST("/profiles")
    fun createProfile(
        @Body profile: ProfileRequest
    ): Completable

    @DELETE("/profiles/{profile_id}")
    fun deleteProfile(
        @Path("profile_id") profileId: String
    ): Completable

    @PUT("/profiles/{profile_id}")
    fun updateProfile(
        @Path("profile_id") profileId: String,
        @Body profile: ProfileRequest
    ): Observable<List<Profile>>
}
