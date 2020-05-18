package com.scudderapps.moviesup.models.movie


import com.google.gson.annotations.SerializedName
import com.scudderapps.moviesup.models.movie.Movie

data class CollectionResponse(
    @SerializedName("backdrop_path")
    val backdropPath: String,
    val id: Int,
    val name: String,
    val overview: String,
    val parts: List<Movie>,
    @SerializedName("poster_path")
    val posterPath: String
)