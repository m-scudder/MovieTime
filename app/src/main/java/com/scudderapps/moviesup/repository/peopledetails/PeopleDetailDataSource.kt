package com.scudderapps.moviesup.repository.peopledetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.scudderapps.moviesup.api.TheTMDBApiInterface
import com.scudderapps.moviesup.models.MovieCredits
import com.scudderapps.moviesup.models.PeopleDetails
import com.scudderapps.moviesup.models.PeopleImages
import com.scudderapps.moviesup.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PeopleDetailDataSource(
    private val apiService: TheTMDBApiInterface,
    private val compositeDisposable: CompositeDisposable
) {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _peopleDetailsResponse = MutableLiveData<PeopleDetails>()
    val peopleDetailsResponse: LiveData<PeopleDetails>
        get() = _peopleDetailsResponse

    private val _movieCreditsResponse = MutableLiveData<MovieCredits>()
    val movieCreditsResponse: LiveData<MovieCredits>
        get() = _movieCreditsResponse

    private val _peopleProfileImages = MutableLiveData<PeopleImages>()
    val peopleProfileImages: LiveData<PeopleImages>
        get() = _peopleProfileImages


    fun fetchPeopleDetails(peopleID: Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            apiService.getPeopleDetails(peopleID)
                ?.subscribeOn(Schedulers.io())
                ?.subscribe(
                    {
                        _peopleDetailsResponse.postValue(it)
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

    fun fetchMovieCredits(peopleID: Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            apiService.getMovieCredits(peopleID)
                ?.subscribeOn(Schedulers.io())
                ?.subscribe(
                    {
                        _movieCreditsResponse.postValue(it)
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

    fun fetchPeopleImages(peopleID: Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            apiService.getPeopleImages(peopleID)
                ?.subscribeOn(Schedulers.io())
                ?.subscribe(
                    {
                        _peopleProfileImages.postValue(it)
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