package dev.dbikic.oktaloginexample.model

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("id") // <1>
    val id: String,
    @SerializedName("email")
    val email: String
)
