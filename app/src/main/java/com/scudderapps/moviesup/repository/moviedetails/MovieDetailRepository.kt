package com.scudderapps.moviesup.repository.moviedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.scudderapps.moviesup.api.TheTMDBApiInterface
import com.scudderapps.moviesup.models.*
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.movielist.MovieDataSource
import io.reactivex.disposables.CompositeDisposable

class MovieDetailRepository(private val apiService: TheTMDBApiInterface) {

    lateinit var movieDetailDataSource: MovieDetailDataSource

    fun fetchingCollection(
        compositeDisposable: CompositeDisposable,
        movieID: Int
    ): LiveData<CollectionResponse> {
        movieDetailDataSource =
            MovieDetailDataSource(
                apiService,
                compositeDisposable
            )
        movieDetailDataSource.fetchCollections(movieID)

        return movieDetailDataSource.collectionResponse
    }


    fun fetchingMoviesDetails(
        compositeDisposable: CompositeDisposable,
        movieID: Int
    ): LiveData<MovieDetail> {
        movieDetailDataSource =
            MovieDetailDataSource(
                apiService,
                compositeDisposable
            )
        movieDetailDataSource.fetchMovieDetails(movieID)

        return movieDetailDataSource.movieDetailsResponse
    }

    fun fetchingMoviesVideos(
        compositeDisposable: CompositeDisposable,
        movieID: Int
    ): LiveData<VideoResponse> {
        movieDetailDataSource =
            MovieDetailDataSource(
                apiService,
                compositeDisposable
            )
        movieDetailDataSource.fetchMovieVideos(movieID)

        return movieDetailDataSource.movieVideoResponse
    }

    fun fetchingCastDetails(
        compositeDisposable: CompositeDisposable,
        movieID: Int
    ): LiveData<CastResponse> {
        movieDetailDataSource =
            MovieDetailDataSource(
                apiService,
                compositeDisposable
            )
        movieDetailDataSource.fetchCastDetails(movieID)

        return movieDetailDataSource.movieCastResponse
    }

    fun getMovieDetailNetworkState(): LiveData<NetworkState> {
        return movieDetailDataSource.networkState
    }

    fun fetchingMovieMedia(
        compositeDisposable: CompositeDisposable,
        movieID: Int
    ): LiveData<MediaResponse> {
        movieDetailDataSource =
            MovieDetailDataSource(
                apiService,
                compositeDisposable
            )
        movieDetailDataSource.fetchMoviesMedia(movieID)

        return movieDetailDataSource.movieMediaResponse
    }
}