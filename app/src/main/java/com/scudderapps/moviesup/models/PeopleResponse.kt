package com.scudderapps.moviesup.models


import com.google.gson.annotations.SerializedName

data class PeopleResponse(
    val page: Int,
    @SerializedName("results")
    val people: List<People>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)