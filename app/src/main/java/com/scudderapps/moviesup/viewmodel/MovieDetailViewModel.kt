package com.scudderapps.moviesup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.scudderapps.moviesup.models.MovieDetail
import com.scudderapps.moviesup.repository.moviedetails.MovieDetailRepository
import com.scudderapps.moviesup.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieDetailViewModel(private val movieDetailRepository: MovieDetailRepository, movieId: Int) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val movieDetails: LiveData<MovieDetail> by lazy {
        movieDetailRepository.fetchingMoviesDetails(compositeDisposable, movieId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieDetailRepository.getMovieDetailNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}