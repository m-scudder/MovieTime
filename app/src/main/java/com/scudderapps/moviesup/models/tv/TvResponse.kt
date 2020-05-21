package com.scudderapps.moviesup.models.tv


import com.google.gson.annotations.SerializedName

data class TvResponse(
    val page: Int,
    @SerializedName("results")
    val tvList: List<TV>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)