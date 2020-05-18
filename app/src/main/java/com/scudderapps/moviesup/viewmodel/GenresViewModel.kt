package com.scudderapps.moviesup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.scudderapps.moviesup.models.main.GenresResponse
import com.scudderapps.moviesup.repository.movie.genre.GenreRepository
import io.reactivex.disposables.CompositeDisposable

class GenresViewModel(private val genreRepository: GenreRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val genresList: LiveData<GenresResponse> by lazy {
        genreRepository.fetchingGenresResponse(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}