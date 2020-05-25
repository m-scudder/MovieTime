package com.scudderapps.moviesup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.scudderapps.moviesup.models.common.GenresResponse
import com.scudderapps.moviesup.repository.genre.GenreRepository
import io.reactivex.disposables.CompositeDisposable

class GenresViewModel(private val genreRepository: GenreRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val movieGenresList: LiveData<GenresResponse> by lazy {
        genreRepository.fetchingMovieGenresResponse(compositeDisposable)
    }

    val tvGenresList: LiveData<GenresResponse> by lazy {
        genreRepository.fetchingTvGenresResponse(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}