package com.scudderapps.moviesup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.scudderapps.moviesup.models.main.People
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.discover.PeoplePagedListRepository
import io.reactivex.disposables.CompositeDisposable

class PeopleListViewModel(
    private val peopleRepository: PeoplePagedListRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val peoplePagedList: LiveData<PagedList<People>> by lazy {
        peopleRepository.fetchingPeopleList(compositeDisposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        peopleRepository.getNetworkState()
    }

    fun peopleListIsEmpty(): Boolean {
        return peoplePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}