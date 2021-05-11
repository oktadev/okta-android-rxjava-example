package dev.dbikic.oktaloginexample.model

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("id")
    val id: String,
    @SerializedName("email")
    val email: String
)
