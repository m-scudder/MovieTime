package com.scudderapps.moviesup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.scudderapps.moviesup.models.main.Movie
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.movie.movielist.MoviePagedListRepository
import io.reactivex.disposables.CompositeDisposable

class MovieListViewModel(private val movieRepository: MoviePagedListRepository, private val type: String) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val moviePagedList: LiveData<PagedList<Movie>> by lazy {
        movieRepository.fetchingMovieList(compositeDisposable, type)
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieRepository.getNetworkState()
    }

    fun movieListIsEmpty(): Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}