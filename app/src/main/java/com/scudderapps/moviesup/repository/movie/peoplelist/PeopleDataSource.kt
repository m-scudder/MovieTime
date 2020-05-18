package com.scudderapps.moviesup.repository.movie.peoplelist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.scudderapps.moviesup.api.FIRST_PAGE
import com.scudderapps.moviesup.api.MovieApiInterface
import com.scudderapps.moviesup.models.main.People
import com.scudderapps.moviesup.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PeopleDataSource(
    private val apiService: MovieApiInterface,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, People>() {

    var page = FIRST_PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, People>
    ) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getPeople(page)
                ?.subscribeOn(Schedulers.io())
                ?.subscribe(
                    {
                        callback.onResult(it.people, null, page + 1)
                        networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("PeopleDataSource", it.message)
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, People>) {

        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getPeople(params.key)
                ?.subscribeOn(Schedulers.io())
                ?.subscribe(
                    {
                        if (it.totalPages >= params.key) {
                            callback.onResult(it.people, params.key + 1)
                            networkState.postValue(NetworkState.LOADED)
                        } else {
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("PeopleDataSource", it.message)
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, People>) {

    }

}