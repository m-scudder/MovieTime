package com.scudderapps.moviesup.models.main


import com.google.gson.annotations.SerializedName
import com.scudderapps.moviesup.models.main.People

data class PeopleResponse(
    val page: Int,
    @SerializedName("results")
    val people: List<People>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)