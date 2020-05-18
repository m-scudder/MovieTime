package com.scudderapps.moviesup.repository.tv

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.api.POST_PER_PAGE
import com.scudderapps.moviesup.models.tv.TV
import com.scudderapps.moviesup.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class TvPagedListRepository(private val apiService: ApiInterface) {

    lateinit var tvPageList: LiveData<PagedList<TV>>
    lateinit var tvDataSourceFactory: TvDataSourceFactory

    fun fetchingMovieList(
        compositeDisposable: CompositeDisposable,
        type: String
    ): LiveData<PagedList<TV>> {

        tvDataSourceFactory = TvDataSourceFactory(apiService, compositeDisposable, type)
        val config = PagedList.Config
            .Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        tvPageList = LivePagedListBuilder(tvDataSourceFactory, config).build()
        return tvPageList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<TvDataSource, NetworkState>(
            tvDataSourceFactory.tvLiveDataSource, TvDataSource::networkState
        )
    }
}