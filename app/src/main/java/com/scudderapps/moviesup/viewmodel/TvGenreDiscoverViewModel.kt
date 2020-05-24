package com.scudderapps.moviesup.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.scudderapps.moviesup.models.main.TV
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.discovergenres.tvshows.TvDiscoverPagedListRepository
import io.reactivex.disposables.CompositeDisposable

class TvGenreDiscoverViewModel(
    private val discoverTvPagedListRepository: TvDiscoverPagedListRepository,
    private val id: Int
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val discoverTvList: LiveData<PagedList<TV>> by lazy {
        discoverTvPagedListRepository.fetchingTvList(compositeDisposable, id)
    }

    fun listIsEmpty(): Boolean {
        return discoverTvList.value?.isEmpty() ?: true
    }

    val networkState: LiveData<NetworkState> by lazy {
        discoverTvPagedListRepository.getNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}