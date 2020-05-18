package com.scudderapps.moviesup.repository.movie.genre

import androidx.lifecycle.LiveData
import com.scudderapps.moviesup.api.MovieApiInterface
import com.scudderapps.moviesup.models.main.GenresResponse
import io.reactivex.disposables.CompositeDisposable

class GenreRepository(private val apiService: MovieApiInterface) {

    lateinit var genresDataSource: GenreDataSource

    fun fetchingGenresResponse(
        compositeDisposable: CompositeDisposable
    ): LiveData<GenresResponse> {
        genresDataSource =
            GenreDataSource(
                apiService,
                compositeDisposable
            )
        genresDataSource.fetchGenresList()

        return genresDataSource.genresResponse
    }
}