package com.scudderapps.moviesup

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.scudderapps.moviesup.adapter.movie.MoviePageListAdapter
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.discovergenres.movies.MovieDiscoverPagedListRepository
import com.scudderapps.moviesup.viewmodel.MovieGenreDiscoverViewModel

class MovieGenreActivity : AppCompatActivity() {

    @BindView(R.id.discover_view)
    lateinit var discoverView: RecyclerView

    @BindView(R.id.discover_toolbar)
    lateinit var discoverToolbar: Toolbar

    @BindView(R.id.discover_progress_bar)
    lateinit var discoverProgressBar: ProgressBar

    private lateinit var discoverViewModel: MovieGenreDiscoverViewModel
    lateinit var moviePagedListRepository: MovieDiscoverPagedListRepository
    private val discoverAdapter =
        MoviePageListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_genre)
        ButterKnife.bind(this)
        setSupportActionBar(discoverToolbar)

        val data = intent.extras
        var id = data!!.getInt("id")
        var name = data!!.getString("name")
        supportActionBar!!.title = "$name Movies"
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        val apiService: ApiInterface = TheTMDBClient.getClient()
        moviePagedListRepository =
            MovieDiscoverPagedListRepository(
                apiService
            )

        discoverViewModel = discoverViewModel(id)
        val layoutManager = GridLayoutManager(this, 3)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = discoverAdapter.getItemViewType(position)
                return if (viewType == discoverAdapter.POPULAR_MOVIE_VIEW_TYPE) 1
                else 3
            }

        }

        discoverViewModel.discoverList.observe(this, Observer {
            discoverAdapter.submitList(it)
            discoverView.layoutManager = layoutManager
            discoverView.setHasFixedSize(true)
            discoverView.adapter = discoverAdapter
        })

        discoverViewModel.networkState.observe(this, Observer {

            discoverProgressBar.visibility =
                if (discoverViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            if (!discoverViewModel.listIsEmpty()) {
                discoverAdapter.setNetworkState(it)
            }
        })

    }

    private fun discoverViewModel(id: Int): MovieGenreDiscoverViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieGenreDiscoverViewModel(moviePagedListRepository, id) as T
            }
        })[MovieGenreDiscoverViewModel::class.java]
    }
}