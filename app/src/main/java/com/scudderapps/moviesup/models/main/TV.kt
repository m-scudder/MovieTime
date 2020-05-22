package com.scudderapps.moviesup.models.main


import com.google.gson.annotations.SerializedName

data class TV(

    val id: Int,
    val name: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("vote_average")
    val voteAverage: Double
)