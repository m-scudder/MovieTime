package com.scudderapps.moviesup.models.common

import com.scudderapps.moviesup.models.main.Movie


data class MovieCredits(
    val cast: List<Movie>,
    val id: Int
)