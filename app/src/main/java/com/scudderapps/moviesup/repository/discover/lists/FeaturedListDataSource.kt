package com.scudderapps.moviesup.repository.discover.lists

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.scudderapps.moviesup.api.TmdbApiInterface
import com.scudderapps.moviesup.models.featuredlist.FeatureLists
import com.scudderapps.moviesup.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FeaturedListDataSource(
    private val apiService: TmdbApiInterface,
    private val compositeDisposable: CompositeDisposable,
    private val listId: Int
) {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _topMovieResponse = MutableLiveData<FeatureLists>()
    val topMovieResponse: LiveData<FeatureLists>
        get() = _topMovieResponse

    fun fetchFeaturedMovies() {
        _networkState.postValue(NetworkState.LOADING)

        try {
            apiService.getFeaturedMovies(listId)
                ?.subscribeOn(Schedulers.io())
                ?.subscribe(
                    {
                        _topMovieResponse.postValue(it)
                        _networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        _networkState.postValue(NetworkState.ERROR)
                        Log.e("FeaturedListDataSource", it.message)
                    }
                )?.let {
                    compositeDisposable.add(
                        it
                    )
                }
        } catch (e: Exception) {
            Log.e("FeaturedListDataSource", e.message)
        }
    }
}