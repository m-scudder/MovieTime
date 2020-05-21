package com.scudderapps.moviesup.models.movie

import com.google.gson.annotations.SerializedName
import com.scudderapps.moviesup.models.movie.VideoDetail


data class VideoResponse(
    val id: Int,
    @SerializedName("results")
    val videosList: ArrayList<VideoDetail>
)