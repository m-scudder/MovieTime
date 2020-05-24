package com.scudderapps.moviesup.repository.tv.tvlist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.api.FIRST_PAGE
import com.scudderapps.moviesup.models.main.TV
import com.scudderapps.moviesup.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TvDataSource(
    private val apiService: ApiInterface,
    private val compositeDisposable: CompositeDisposable,
    private val type: String
) : PageKeyedDataSource<Int, TV>() {

    var page = FIRST_PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, TV>
    ) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getTVList(type, page)
                ?.subscribeOn(Schedulers.io())
                ?.subscribe(
                    {
                        callback.onResult(it.tvList, null, page + 1)
                        networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource", it.message)
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, TV>) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getTVList(type, params.key)
                ?.subscribeOn(Schedulers.io())
                ?.subscribe(
                    {
                        if (it.totalPages >= params.key) {
                            callback.onResult(it.tvList, params.key + 1)
                            networkState.postValue(NetworkState.LOADED)
                        } else {
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource", it.message)
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, TV>) {

    }

}