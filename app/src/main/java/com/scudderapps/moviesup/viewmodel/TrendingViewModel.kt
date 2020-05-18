package com.scudderapps.moviesup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.scudderapps.moviesup.models.movie.Movie
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.movie.trending.TrendingPagedListRepository
import io.reactivex.disposables.CompositeDisposable

class TrendingViewModel(
    private val trendingPagedListRepository: TrendingPagedListRepository,
    private val type: String
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val trendingList: LiveData<PagedList<Movie>> by lazy {
        trendingPagedListRepository.fetchingTrendingMovieList(compositeDisposable, type)
    }

    fun listIsEmpty(): Boolean {
        return trendingList.value?.isEmpty() ?: true
    }

    val networkState: LiveData<NetworkState> by lazy {
        trendingPagedListRepository.getNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}