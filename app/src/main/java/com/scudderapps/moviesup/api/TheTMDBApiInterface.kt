package com.scudderapps.moviesup.api

import com.scudderapps.moviesup.models.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryName

interface TheTMDBApiInterface {

    @GET("movie/{type}?region=in")
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

    @GET("movie/{id}/images")
    fun getMovieMedia(
        @Path("id") movieId: Int
    ): Single<MediaResponse>

    @GET("search/movie")
    fun getSearchResults(
        @Query("query") query: String
    ): Single<MovieResponse>

    @GET("person/popular")
    fun getPeople(
        @Query("page") page: Int
    ): Single<PeopleResponse>

    @GET("person/{id}")
    fun getPeopleDetails(
        @Path("id") id: Int
    ): Single<PeopleDetails>

    @GET("person/{id}/movie_credits")
    fun getMovieCredits(
        @Path("id") id: Int
    ): Single<MovieCredits>

    @GET("person/{id}/images")
    fun getPeopleImages(
        @Path("id") id: Int
    ): Single<PeopleImages>

    @GET("genre/movie/list")
    fun getGenresList(): Single<GenresResponse>

}