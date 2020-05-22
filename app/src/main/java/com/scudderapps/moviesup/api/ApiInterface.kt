package com.scudderapps.moviesup.api

import com.scudderapps.moviesup.models.main.GenresResponse
import com.scudderapps.moviesup.models.main.PeopleDetails
import com.scudderapps.moviesup.models.main.PeopleImages
import com.scudderapps.moviesup.models.main.PeopleResponse
import com.scudderapps.moviesup.models.movie.*
import com.scudderapps.moviesup.models.tv.TvDetail
import com.scudderapps.moviesup.models.tv.TvResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

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

    @GET("discover/movie?&region=in&sort_by=popularity.desc")
    fun getDiscoveredMovies(
        @Query("with_genres") id: Int,
        @Query("page") page: Int
    ): Single<MovieResponse>

    @GET("trending/movie/{type}")
    fun getTrendingList(
        @Path("type") type: String,
        @Query("page") page: Int
    ): Single<MovieResponse>

    @GET("collection/{id}")
    fun getCollections(
        @Path("id") id: Int
    ): Single<CollectionResponse>

    @GET("tv/{type}?region=in")
    fun getTVList(
        @Path("type") type: String,
        @Query("page") page: Int
    ): Single<TvResponse>

    @GET("tv/{tv_id}")
    fun getTVDetails(
        @Path("tv_id") tvId: Int
    ): Single<TvDetail>

    @GET("tv/{tv_id}/videos")
    fun getTvVideos(
        @Path("tv_id") tvId: Int
    ): Single<VideoResponse>

    @GET("tv/{tv_id}/images")
    fun getTvMedia(
        @Path("tv_id") tvId: Int
    ): Single<MediaResponse>

    @GET("tv/{tv_id}/credits")
    fun getTvCast(
        @Path("tv_id") tvId : Int
    ): Single<CastResponse>
}