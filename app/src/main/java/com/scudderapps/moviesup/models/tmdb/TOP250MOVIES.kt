package com.scudderapps.moviesup.models.tmdb


data class TOP250MOVIES(
    val errorMessage: String,
    val items: List<Item>
)