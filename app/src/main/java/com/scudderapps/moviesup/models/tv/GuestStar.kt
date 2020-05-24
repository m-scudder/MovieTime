package com.scudderapps.moviesup.models.tv


import com.google.gson.annotations.SerializedName

data class GuestStar(
    val character: String,
    @SerializedName("credit_id")
    val creditId: String,
    val gender: Int,
    val id: Int,
    val name: String,
    val order: Int,
    @SerializedName("profile_path")
    val profilePath: String
)