package com.scudderapps.moviesup.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.adapter.movie.MoviePageListAdapter
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.movie.movielist.MoviePagedListRepository
import com.scudderapps.moviesup.viewmodel.MovieListViewModel

class MovieListFragment(private val type: String) : Fragment() {

    @BindView(R.id.movie_list_view)
    lateinit var movieView: RecyclerView

    @BindView(R.id.main_progress_bar)
    lateinit var mainProgressBar: ProgressBar

    private lateinit var rootView: View
    private lateinit var movieAdapter: MoviePageListAdapter
    private lateinit var listViewModel: MovieListViewModel
    lateinit var moviePagedListRepository: MoviePagedListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieAdapter =
            MoviePageListAdapter(
                activity!!.applicationContext
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.movie_list_fragment, container, false)
        ButterKnife.bind(this, rootView)
        val apiService: ApiInterface = TheTMDBClient.getClient()
        moviePagedListRepository = MoviePagedListRepository(apiService)
        listViewModel = movieListViewModel(type)
        populatingViews()

        return rootView
    }

    private fun movieListViewModel(type: String): MovieListViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieListViewModel(moviePagedListRepository, type) as T
            }
        })[MovieListViewModel::class.java]
    }

    private fun populatingViews() {
        listViewModel.moviePagedList.observe(viewLifecycleOwner, Observer {
            movieAdapter.submitList(it)
            val layoutManager = GridLayoutManager(activity, 4)
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val viewType = movieAdapter.getItemViewType(position)
                    return if (viewType == movieAdapter.POPULAR_MOVIE_VIEW_TYPE) 1
                    else 4
                }
            }
            movieView.layoutManager = layoutManager
            movieView.setHasFixedSize(true)
            movieView.adapter = movieAdapter

        })

        listViewModel.networkState.observe(viewLifecycleOwner, Observer {

            mainProgressBar.visibility =
                if (listViewModel.movieListIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            if (!listViewModel.movieListIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })
    }
}