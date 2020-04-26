package com.scudderapps.moviesup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.scudderapps.moviesup.models.MovieCredits
import com.scudderapps.moviesup.models.PeopleDetails
import com.scudderapps.moviesup.models.PeopleImages
import com.scudderapps.moviesup.repository.peopledetails.PeopleDetailRepository
import io.reactivex.disposables.CompositeDisposable

class PeopleDetailViewModel(
    private val peopleDetailRepository: PeopleDetailRepository,
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}