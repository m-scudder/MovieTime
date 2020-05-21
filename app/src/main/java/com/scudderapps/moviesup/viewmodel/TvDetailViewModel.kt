package com.scudderapps.moviesup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.scudderapps.moviesup.models.tv.TvDetail
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.tv.TvDetailRepository
import io.reactivex.disposables.CompositeDisposable

class TvDetailViewModel(private val tvDetailRepository: TvDetailRepository, tvId: Int) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val tvDetails: LiveData<TvDetail> by lazy {
        tvDetailRepository.fetchingTvDetails(compositeDisposable, tvId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        tvDetailRepository.getTvDetailNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}