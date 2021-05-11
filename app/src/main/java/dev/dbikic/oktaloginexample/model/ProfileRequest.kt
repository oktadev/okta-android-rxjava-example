package dev.dbikic.oktaloginexample.model

import com.google.gson.annotations.SerializedName

data class ProfileRequest(
    @SerializedName("email")
    val email: String
)
