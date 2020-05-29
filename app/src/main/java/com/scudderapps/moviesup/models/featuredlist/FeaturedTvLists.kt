package com.scudderapps.moviesup.models.featuredlist


import com.google.gson.annotations.SerializedName
import com.scudderapps.moviesup.models.main.TV

data class FeaturedTvLists(
    @SerializedName("created_by")
    val createdBy: String,
    val description: String,
    @SerializedName("favorite_count")
    val favoriteCount: Int,
    val id: String,
    @SerializedName("iso_639_1")
    val iso6391: String,
    @SerializedName("item_count")
    val itemCount: Int,
    val items: List<TV>,
    val name: String,
    @SerializedName("poster_path")
    val posterPath: Any
)