package com.scudderapps.moviesup.models.movie


import com.google.gson.annotations.SerializedName

data class ReleaseDatesDetails(
    @SerializedName("iso_3166_1")
    val iso31661: String,
    @SerializedName("release_dates")
    val releaseDates: List<ReleaseDateInformation>
)