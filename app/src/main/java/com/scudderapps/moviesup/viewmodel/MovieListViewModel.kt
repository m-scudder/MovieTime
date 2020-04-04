package com.scudderapps.moviesup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.scudderapps.moviesup.models.Movie
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.movielist.MoviePagedListRepository
import io.reactivex.disposables.CompositeDisposable

class MovieListViewModel(private val movieRepository: MoviePagedListRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val popularMoviePagedList: LiveData<PagedList<Movie>> by lazy {
        movieRepository.fetchingMovieList(compositeDisposable, "popular")
    }
    val networkState: LiveData<NetworkState> by lazy {
        movieRepository.getNerworkState()
    }

    fun listIsEmpty(): Boolean {

        return popularMoviePagedList.value?.isEmpty() ?:true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}