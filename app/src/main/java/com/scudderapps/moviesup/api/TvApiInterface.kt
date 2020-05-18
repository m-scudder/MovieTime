package com.scudderapps.moviesup.api

import com.scudderapps.moviesup.models.tv.TvResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvApiInterface {

    @GET("tv/popular?region=in")
    fun getMovieList(
        @Path("type") type: String,
        @Query("page") page: Int
    ): Single<TvResponse>
}