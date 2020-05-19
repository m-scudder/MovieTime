package com.scudderapps.moviesup.fragments.moviedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import butterknife.BindView
import butterknife.ButterKnife
import com.ms.square.android.expandabletextview.ExpandableTextView
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.adapter.movie.MoviePageListAdapter
import com.scudderapps.moviesup.adapter.movie.moviedetails.TrailerListAdapter
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.models.main.Genre
import com.scudderapps.moviesup.models.movie.VideoDetail
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.movie.moviedetails.MovieDetailRepository
import com.scudderapps.moviesup.repository.movie.movielist.MoviePagedListRepository
import com.scudderapps.moviesup.viewmodel.MovieDetailViewModel

class AboutMovieFragment(private var movieId: Int) : Fragment() {

    private lateinit var rootView: View

    @BindView(R.id.movie_overview)
    lateinit var movieOverview: ExpandableTextView

    @BindView(R.id.genresName)
    lateinit var genresName: TextView

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var movieRepository: MovieDetailRepository

    private lateinit var trailerAdapter: TrailerListAdapter
    private lateinit var trailerDetail: ArrayList<VideoDetail>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.about_movie_fragment, container, false)
        ButterKnife.bind(this, rootView)
        val apiService: ApiInterface = TheTMDBClient.getClient()
        movieRepository = MovieDetailRepository(apiService)
        viewModel = getViewModel(movieId)
        populatingViews()
        return rootView
    }

    private fun populatingViews() {
        viewModel.movieDetails.observe(this, Observer {
            movieOverview.text = it.overview
            val genre: ArrayList<Genre> = it.genres
            if (!genre.isNullOrEmpty()) {
                for (i in genre) {
                    genresName.append("\u25CF ${i.name}  ")
                }
            } else {
//                genresLayout.visibility = View.GONE
            }
        })
    }
    private fun getViewModel(movieId: Int): MovieDetailViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieDetailViewModel(movieRepository, movieId) as T
            }
        })[MovieDetailViewModel::class.java]
    }
}
