package com.scudderapps.moviesup.repository.tv.tvlist

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.scudderapps.moviesup.api.TmdbApiInterface
import com.scudderapps.moviesup.models.main.TV
import io.reactivex.disposables.CompositeDisposable

class TvDataSourceFactory(
    private val apiService: TmdbApiInterface,
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