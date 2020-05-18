package com.scudderapps.moviesup.repository.tv

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.models.tv.TV
import io.reactivex.disposables.CompositeDisposable

class TvDataSourceFactory(
    private val apiService: ApiInterface,
    private val compositeDisposable: CompositeDisposable,
    private val type: String
) : DataSource.Factory<Int, TV>() {

    val tvLiveDataSource = MutableLiveData<TvDataSource>()

    override fun create(): DataSource<Int, TV> {
        val tvDataSource =
            TvDataSource(
                apiService,
                compositeDisposable,
                type
            )

        tvLiveDataSource.postValue(tvDataSource)
        return tvDataSource
    }
}