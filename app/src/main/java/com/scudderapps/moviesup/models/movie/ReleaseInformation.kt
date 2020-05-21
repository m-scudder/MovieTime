package com.scudderapps.moviesup.models.movie


import com.google.gson.annotations.SerializedName

data class ReleaseInformation(
    val id: Int,
    @SerializedName("results")
    val releaseDateList: List<ReleaseDatesDetails>
)