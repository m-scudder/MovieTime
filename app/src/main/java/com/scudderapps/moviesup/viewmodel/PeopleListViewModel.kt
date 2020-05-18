package com.scudderapps.moviesup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.scudderapps.moviesup.models.main.People
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.peoplelist.PeoplePagedListRepository
import io.reactivex.disposables.CompositeDisposable

class PeopleListViewModel(private val peopleListRepository: PeoplePagedListRepository) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val popularPeoplePagedList: LiveData<PagedList<People>> by lazy {
        peopleListRepository.fetchingPeopleList(compositeDisposable)
    }

    fun peopleListIsEmpty(): Boolean {
        return popularPeoplePagedList.value?.isEmpty() ?: true
    }

    val networkState: LiveData<NetworkState> by lazy {
        peopleListRepository.getNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}