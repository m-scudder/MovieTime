package com.scudderapps.moviesup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.scudderapps.moviesup.models.PeopleDetails
import com.scudderapps.moviesup.repository.peopledetails.PeopleDetailRepository
import io.reactivex.disposables.CompositeDisposable

class PeopleViewModel(private val peopleDetailRepository: PeopleDetailRepository, peopleId: Int) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val peopleDetails: LiveData<PeopleDetails> by lazy {
        peopleDetailRepository.fetchingPeoplesDetails(compositeDisposable, peopleId)
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}