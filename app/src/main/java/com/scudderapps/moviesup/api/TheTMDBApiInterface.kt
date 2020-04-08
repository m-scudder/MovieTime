package com.scudderapps.moviesup.api

import com.scudderapps.moviesup.models.CastResponse
import com.scudderapps.moviesup.models.MovieDetail
import com.scudderapps.moviesup.models.MovieResponse
import com.scudderapps.moviesup.models.VideoResponse
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

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") movieId: Int
    ): Single<MovieDetail>

    @GET("movie/{id}/videos")
    fun getMovieVideos(
        @Path("id") movieId: Int
    ): Single<VideoResponse>

    @GET("movie/{id}/credits")
    fun getMovieCast(
        @Path("id") movieId: Int
    ): Single<CastResponse>

    @GET("search/movie/{query}")
    fun getMPersonDetails(
        @Query("query") query: Query
    )
}