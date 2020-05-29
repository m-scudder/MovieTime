package com.scudderapps.moviesup.repository.discovergenres.tvshows

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.scudderapps.moviesup.api.TmdbApiInterface
import com.scudderapps.moviesup.api.POST_PER_PAGE
import com.scudderapps.moviesup.models.main.TV
import com.scudderapps.moviesup.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class TvDiscoverPagedListRepository(private val apiService: TmdbApiInterface) {

    lateinit var tvPageList: LiveData<PagedList<TV>>
    lateinit var discoverTvDataSourceFactory: TvDiscoverDataSourceFactory

    fun fetchingTvList(
        compositeDisposable: CompositeDisposable,
        id: Int
    ): LiveData<PagedList<TV>> {

        discoverTvDataSourceFactory =
            TvDiscoverDataSourceFactory(apiService, compositeDisposable, id)
        val config = PagedList.Config
            .Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        tvPageList = LivePagedListBuilder(discoverTvDataSourceFactory, config).build()
        return tvPageList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<TvDiscoverDataSource, NetworkState>(
            discoverTvDataSourceFactory.discoverTvLiveDataSource, TvDiscoverDataSource::networkState
        )
    }
}