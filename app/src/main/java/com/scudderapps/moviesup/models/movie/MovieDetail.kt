package com.scudderapps.moviesup.models.movie

import com.google.gson.annotations.SerializedName
import com.scudderapps.moviesup.models.main.Genre

data class MovieDetail(
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("belongs_to_collection")
    val belongsToCollection: CollectionResponse,
    val genres: ArrayList<Genre>,
    val homepage: String,
    val id: Int,
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val runtime: Int,
    val status: String,
    val title: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
)