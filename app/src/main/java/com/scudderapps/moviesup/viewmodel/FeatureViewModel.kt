package com.scudderapps.moviesup.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.scudderapps.moviesup.models.tmdb.TOP250MOVIES
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.discover.lists.FeaturedListRepository
import io.reactivex.disposables.CompositeDisposable

class FeatureViewModel(private val featuredListRepository: FeaturedListRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val top250MoviesList: LiveData<TOP250MOVIES> by lazy {
        featuredListRepository.fetchingTopMovies(compositeDisposable)
    }
    val getNetworkState:LiveData<NetworkState> by lazy {
        featuredListRepository.getTvSeasonDetailNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}