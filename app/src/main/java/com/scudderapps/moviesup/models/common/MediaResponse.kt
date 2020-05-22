package com.scudderapps.moviesup.models.common


data class MediaResponse(
    val backdrops: List<Backdrop>,
    val id: Int,
    val posters: List<Poster>
)