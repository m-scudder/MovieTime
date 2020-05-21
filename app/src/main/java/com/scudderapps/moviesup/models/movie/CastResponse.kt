package com.scudderapps.moviesup.models.movie

import com.google.gson.annotations.SerializedName


data class CastResponse(
    @SerializedName("cast")
    val castDetail: ArrayList<CastDetail>,
    @SerializedName("crew")
    val crewDetail: ArrayList<CrewDetail>,
    val id: Int
)