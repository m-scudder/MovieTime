package com.scudderapps.moviesup.models

import com.google.gson.annotations.SerializedName


data class VideoResponse(
    val id: Int,
    @SerializedName("results")
    val videosList: ArrayList<VideoDetail>
)