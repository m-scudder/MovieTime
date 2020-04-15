package com.scudderapps.moviesup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.scudderapps.moviesup.models.Movie
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.discovery.DiscoverPagedListRepository
import io.reactivex.disposables.CompositeDisposable

class DiscoverViewModel(
    private val discoverPagedListRepository: DiscoverPagedListRepository,
    private val id: Int
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val discoverList: LiveData<PagedList<Movie>> by lazy {
        discoverPagedListRepository.fetchingMovieList(compositeDisposable, id)
    }

    fun listIsEmpty(): Boolean {
        return discoverList.value?.isEmpty() ?: true
    }

    val networkState: LiveData<NetworkState> by lazy {
        discoverPagedListRepository.getNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}