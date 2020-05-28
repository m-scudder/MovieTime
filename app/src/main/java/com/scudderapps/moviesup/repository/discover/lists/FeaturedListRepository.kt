package com.scudderapps.moviesup.repository.discover.lists


import androidx.lifecycle.LiveData
import com.scudderapps.moviesup.api.TmdbApiInterface
import com.scudderapps.moviesup.models.featuredlist.FeatureLists
import com.scudderapps.moviesup.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class FeaturedListRepository(private val apiService: TmdbApiInterface) {

    lateinit var featuredDetailDataSource: FeaturedListDataSource

    fun fetchingTopMovies(
        compositeDisposable: CompositeDisposable
    ): LiveData<FeatureLists> {
        featuredDetailDataSource =
            FeaturedListDataSource(
                apiService,
                compositeDisposable
            )
        featuredDetailDataSource.fetch250Movies()

        return featuredDetailDataSource.topMovieResponse
    }

    fun getTvSeasonDetailNetworkState(): LiveData<NetworkState> {
        return featuredDetailDataSource.networkState
    }
}