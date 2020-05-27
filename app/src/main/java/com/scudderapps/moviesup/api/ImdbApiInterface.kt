package com.scudderapps.moviesup.api

import com.scudderapps.moviesup.models.tmdb.Item
import com.scudderapps.moviesup.models.tmdb.TOP250MOVIES
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET

interface ImdbApiInterface {

    @GET("Top250Movies")
    fun getTop250Movies(
    ): Single<TOP250MOVIES>
}