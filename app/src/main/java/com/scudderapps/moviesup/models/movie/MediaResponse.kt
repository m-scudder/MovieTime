package com.scudderapps.moviesup.models.movie


data class MediaResponse(
    val backdrops: List<Backdrop>,
    val id: Int,
    val posters: List<Poster>
)