package com.scudderapps.moviesup.models.common

import com.google.gson.annotations.SerializedName
import com.scudderapps.moviesup.models.common.VideoDetail


data class VideoResponse(
    val id: Int,
    @SerializedName("results")
    val videosList: ArrayList<VideoDetail>
)