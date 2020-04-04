package com.scudderapps.moviesup.repository.moviedetails

import androidx.lifecycle.LiveData
import com.scudderapps.moviesup.api.TheTMDBApiInterface
import com.scudderapps.moviesup.models.MovieDetail
import com.scudderapps.moviesup.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieDetailRepository(private val apiService: TheTMDBApiInterface) {

    lateinit var movieDetailDataSource: MovieDetailDataSource

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

    fun getMovieDetailNetworkState(): LiveData<NetworkState> {
        return movieDetailDataSource.netwrokState
    }
}