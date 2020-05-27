package com.scudderapps.moviesup.repository.discover.lists

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.scudderapps.moviesup.api.ImdbApiInterface
import com.scudderapps.moviesup.api.TmdbApiInterface
import com.scudderapps.moviesup.models.common.MovieCredits
import com.scudderapps.moviesup.models.common.TvCredits
import com.scudderapps.moviesup.models.main.PeopleDetails
import com.scudderapps.moviesup.models.main.PeopleImages
import com.scudderapps.moviesup.models.tmdb.Item
import com.scudderapps.moviesup.models.tmdb.TOP250MOVIES
import com.scudderapps.moviesup.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FeaturedListDataSource(
    private val apiService: ImdbApiInterface,
    private val compositeDisposable: CompositeDisposable
) {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _topMovieResponse = MutableLiveData<TOP250MOVIES>()
    val topMovieResponse: LiveData<TOP250MOVIES>
        get() = _topMovieResponse

    fun fetch250Movies() {
        _networkState.postValue(NetworkState.LOADING)

        try {
            apiService.getTop250Movies()
                ?.subscribeOn(Schedulers.io())
                ?.subscribe(
                    {
                        _topMovieResponse.postValue(it)
                        _networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        _networkState.postValue(NetworkState.ERROR)
                        Log.e("PeopleDetailDataSource", it.message)
                    }
                )?.let {
                    compositeDisposable.add(
                        it
                    )
                }
        } catch (e: Exception) {
            Log.e("PeopleDetailDataSource", e.message)
        }
    }
}