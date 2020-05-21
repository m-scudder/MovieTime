package com.scudderapps.moviesup.models.movie

import com.scudderapps.moviesup.models.movie.Movie


data class MovieCredits(
    val cast: List<Movie>,
    val id: Int
)