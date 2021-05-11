package dev.dbikic.oktaloginexample.network

import dev.dbikic.oktaloginexample.model.Profile
import dev.dbikic.oktaloginexample.model.ProfileRequest
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface ProfileService {

    @GET("/profiles") // <1>
    fun getProfiles(): Observable<List<Profile>> // <2>

    @POST("/profiles") // <3>
    fun createProfile(
        @Body profile: ProfileRequest // <4>
    ): Completable // <5>

    @DELETE("/profiles/{profile_id}") // <6>
    fun deleteProfile(
        @Path("profile_id") profileId: String // <7>
    ): Completable

    @PUT("/profiles/{profile_id}") // <8>
    fun updateProfile(
        @Path("profile_id") profileId: String,
        @Body profile: ProfileRequest
    ): Observable<List<Profile>>
}
