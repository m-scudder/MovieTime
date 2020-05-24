package com.scudderapps.moviesup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.scudderapps.moviesup.models.common.CastResponse
import com.scudderapps.moviesup.models.common.MediaResponse
import com.scudderapps.moviesup.models.common.VideoResponse
import com.scudderapps.moviesup.models.tv.TvDetail
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.tv.tvdetails.TvDetailRepository
import io.reactivex.disposables.CompositeDisposable

class TvDetailViewModel(private val tvDetailRepository: TvDetailRepository, tvId: Int) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val tvDetails: LiveData<TvDetail> by lazy {
        tvDetailRepository.fetchingTvDetails(compositeDisposable, tvId)
    }

    val tvVideoDetails: LiveData<VideoResponse> by lazy {
        tvDetailRepository.fetchingTvVideos(compositeDisposable, tvId)
    }

    val tvMedia: LiveData<MediaResponse> by lazy {
        tvDetailRepository.fetchingTvMedia(compositeDisposable, tvId)
    }

    val tvCast: LiveData<CastResponse> by lazy {
        tvDetailRepository.fetchingTvCastDetails(compositeDisposable, tvId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        tvDetailRepository.getTvDetailNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}