package com.scudderapps.moviesup.models


data class MediaResponse(
    val backdrops: List<Backdrop>,
    val id: Int,
    val posters: List<Poster>
)