package com.scudderapps.moviesup.models.movie


import com.google.gson.annotations.SerializedName

data class ReleaseDateInformation(
    val certification: String,
    @SerializedName("iso_639_1")
    val iso6391: String,
    val note: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val type: Int
)