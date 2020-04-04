package com.scudderapps.moviesup

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.scudderapps.moviesup.adapter.MoviePageListAdapter
import com.scudderapps.moviesup.api.TheTMDBApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.movielist.MoviePagedListRepository
import com.scudderapps.moviesup.viewmodel.MovieListViewModel

class MainActivity : AppCompatActivity() {

    @BindView(R.id.main_toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.movieList)
    lateinit var popularMovieView: RecyclerView

    @BindView(R.id.progressBarPopular)
    lateinit var progressBar: ProgressBar

    @BindView(R.id.errorTextPopular)
    lateinit var errorTextView: TextView

    private lateinit var listViewModel: MovieListViewModel
    lateinit var moviePagedListRepository: MoviePagedListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)

        val apiService: TheTMDBApiInterface = TheTMDBClient.getClient()
        moviePagedListRepository = MoviePagedListRepository(apiService)

        val popularAdapter = MoviePageListAdapter(this)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        listViewModel = getViewModel()

        listViewModel.popularMoviePagedList.observe(this, Observer {
            popularAdapter.submitList(it)
            popularMovieView.layoutManager = linearLayoutManager
            popularMovieView.setHasFixedSize(true)
            popularMovieView.adapter = popularAdapter

        })
//        listViewModel.topRatedMoviePagedList.observe(this, Observer {
//            trendingAdapter.submitList(it)
//            trendingMovieView.layoutManager = linearLayoutManager
//            trendingMovieView.setHasFixedSize(true)
//            trendingMovieView.adapter = trendingAdapter
//        })

        listViewModel.networkState.observe(this, Observer {
            progressBar.visibility =
                if (listViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            errorTextView.visibility =
                if (listViewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!listViewModel.listIsEmpty()) {
//                trendingAdapter.setNetworkState(it)
                popularAdapter.setNetworkState(it)
            }
        })
    }

    private fun getViewModel(): MovieListViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieListViewModel(moviePagedListRepository) as T
            }
        })[MovieListViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}