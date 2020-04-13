package com.scudderapps.moviesup.repository.peopledetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.scudderapps.moviesup.api.TheTMDBApiInterface
import com.scudderapps.moviesup.models.PeopleDetails
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
}