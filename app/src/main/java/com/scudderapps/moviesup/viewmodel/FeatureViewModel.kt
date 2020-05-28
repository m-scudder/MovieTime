package com.scudderapps.moviesup.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.scudderapps.moviesup.models.featuredlist.FeatureLists
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.discover.lists.FeaturedListRepository
import io.reactivex.disposables.CompositeDisposable

class FeatureViewModel(
    private val featuredListRepository: FeaturedListRepository,
    private val listId: Int
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val top250MoviesList: LiveData<FeatureLists> by lazy {
        featuredListRepository.fetchingFeaturedMovies(compositeDisposable, listId)
    }
    val getNetworkState: LiveData<NetworkState> by lazy {
        featuredListRepository.getTvSeasonDetailNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}