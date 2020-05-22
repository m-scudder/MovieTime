package com.scudderapps.moviesup.models.tv


import com.google.gson.annotations.SerializedName
import com.scudderapps.moviesup.models.common.CrewDetail

data class Episode(
    @SerializedName("air_date")
    val airDate: String,
    @SerializedName("crew")
    val crewList: List<CrewDetail>,
    @SerializedName("episode_number")
    val episodeNumber: Int,
    @SerializedName("guest_stars")
    val guestStars: List<GuestStar>,
    val id: Int,
    val name: String,
    val overview: String,
    @SerializedName("production_code")
    val productionCode: String,
    @SerializedName("season_number")
    val seasonNumber: Int,
    @SerializedName("show_id")
    val showId: Int,
    @SerializedName("still_path")
    val stillPath: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
)