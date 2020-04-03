package com.scudderapps.moviesup.api

import com.scudderapps.moviesup.models.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheTMDBApiInterface {

    @GET("movie/{type}")
    fun getMovieList(
        @Path("type") type: String,
        @Query("page") page: Int
    ): Single<MovieResponse>

}