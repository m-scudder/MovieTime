package com.scudderapps.moviesup.models.common


import com.scudderapps.moviesup.models.main.TV

data class TvCredits(
    val cast: List<TV>,
    val id: Int
)