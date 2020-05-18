package com.scudderapps.moviesup.models.movie

import com.google.gson.annotations.SerializedName


data class CastResponse(
    @SerializedName("cast")
    val castDetail: ArrayList<CastDetail>,
    val id: Int
)