package com.scudderapps.moviesup.api

import com.scudderapps.moviesup.models.common.*
import com.scudderapps.moviesup.models.featuredlist.FeatureLists
import com.scudderapps.moviesup.models.main.PeopleDetails
import com.scudderapps.moviesup.models.main.PeopleImages
import com.scudderapps.moviesup.models.main.PeopleResponse
import com.scudderapps.moviesup.models.movie.CollectionResponse
import com.scudderapps.moviesup.models.movie.MovieDetail
import com.scudderapps.moviesup.models.tv.TvDetail
import com.scudderapps.moviesup.models.tv.TvResponse
import com.scudderapps.moviesup.models.tv.TvSeasonDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiInterface {

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

    @GET("person/{id}/tv_credits")
    fun getTvCredits(
        @Path("id") id: Int
    ): Single<TvCredits>

    @GET("person/{id}/images")
    fun getPeopleImages(
        @Path("id") id: Int
    ): Single<PeopleImages>

    @GET("genre/movie/list")
    fun getMovieGenresList(): Single<GenresResponse>

    @GET("genre/tv/list")
    fun getTvGenresList(): Single<GenresResponse>

    @GET("discover/movie?&region=in&sort_by=popularity.desc")
    fun getDiscoveredMovies(
        @Query("with_genres") id: Int,
        @Query("page") page: Int
    ): Single<MovieResponse>

    @GET("discover/tv?&region=in&sort_by=popularity.desc")
    fun getDiscoveredTv(
        @Query("with_genres") id: Int,
        @Query("page") page: Int
    ): Single<TvResponse>

    @GET("trending/movie/{type}")
    fun getTrendingList(
        @Path("type") type: String,
        @Query("page") page: Int
    ): Single<MovieResponse>

    @GET("tv/{type}")
    fun getTVList(
        @Path("type") type: String,
        @Query("page") page: Int
    ): Single<TvResponse>

    @GET("collection/{id}")
    fun getCollections(
        @Path("id") id: Int
    ): Single<CollectionResponse>

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
        @Path("tv_id") tvId: Int
    ): Single<CastResponse>

    @GET("tv/{tv_id}/season/{season_number}")
    fun getTvSeason(
        @Path("tv_id") tvId: Int,
        @Path("season_number") seasonNumber: Int
    ): Single<TvSeasonDetails>

    @GET("tv/{tv_id}/season/{season_number}/videos")
    fun getTvSeasonVideos(
        @Path("tv_id") tvId: Int,
        @Path("season_number") seasonNumber: Int
    ): Single<VideoResponse>

    @GET("tv/{tv_id}/season/{season_number}/credits")
    fun getTvSeasonCast(
        @Path("tv_id") tvId: Int,
        @Path("season_number") seasonNumber: Int
    ) : Single<CastResponse>

    @GET("list/144105")
    fun getTop10Movies(): Single<FeatureLists>
}