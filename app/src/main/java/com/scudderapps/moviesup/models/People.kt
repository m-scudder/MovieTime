package com.scudderapps.moviesup.models


import com.google.gson.annotations.SerializedName

data class People(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    @SerializedName("known_for")
    val knownFor: List<Movie>,
    @SerializedName("known_for_department")
    val knownForDepartment: String,
    val name: String,
    val popularity: Double,
    @SerializedName("profile_path")
    val profilePath: String
)