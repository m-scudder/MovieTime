package com.scudderapps.moviesup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.scudderapps.moviesup.models.common.MovieCredits
import com.scudderapps.moviesup.models.common.TvCredits
import com.scudderapps.moviesup.models.main.PeopleDetails
import com.scudderapps.moviesup.models.main.PeopleImages
import com.scudderapps.moviesup.repository.cast.CastDetailRepository
import io.reactivex.disposables.CompositeDisposable

class CastDetailViewModel(
    private val peopleDetailRepository: CastDetailRepository,
    peopleId: Int
) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val peopleDetails: LiveData<PeopleDetails> by lazy {
        peopleDetailRepository.fetchingPeoplesDetails(compositeDisposable, peopleId)
    }

    val movieCredits: LiveData<MovieCredits> by lazy {
        peopleDetailRepository.fetchingMovieCredits(compositeDisposable, peopleId)
    }

    val peopleImages: LiveData<PeopleImages> by lazy {
        peopleDetailRepository.fetchingPeopleImages(compositeDisposable, peopleId)
    }

    val tvCredits  : LiveData<TvCredits> by lazy {
        peopleDetailRepository.fetchingTvCredits(compositeDisposable, peopleId)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}