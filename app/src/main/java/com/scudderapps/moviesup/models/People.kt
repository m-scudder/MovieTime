package com.scudderapps.moviesup.models


import com.google.gson.annotations.SerializedName

data class People(
    val id: Int,
    val name: String,
    @SerializedName("profile_path")
    val profilePath: String
)