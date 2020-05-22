package com.scudderapps.moviesup.models.common


import com.google.gson.annotations.SerializedName
import com.scudderapps.moviesup.models.main.Movie

data class MovieResponse(

    val page: Int,
    @SerializedName("results")
    val movieList: List<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)