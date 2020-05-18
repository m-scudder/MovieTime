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
import com.scudderapps.moviesup.adapter.movieadapter.MoviePageListAdapter
import com.scudderapps.moviesup.api.MovieApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.movie.discovery.DiscoverPagedListRepository
import com.scudderapps.moviesup.viewmodel.DiscoverViewModel

class DiscoverMovie : AppCompatActivity() {

    @BindView(R.id.discover_view)
    lateinit var discoverView: RecyclerView

    @BindView(R.id.discover_toolbar)
    lateinit var discoverToolbar: Toolbar

    @BindView(R.id.discover_progress_bar)
    lateinit var discoverProgressBar: ProgressBar

    private lateinit var discoverViewModel: DiscoverViewModel
    lateinit var moviePagedListRepository: DiscoverPagedListRepository
    private val discoverAdapter =
        MoviePageListAdapter(this)
    private val layoutManager = GridLayoutManager(this, 4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.discover_movies)
        ButterKnife.bind(this)
        setSupportActionBar(discoverToolbar)

        val data = intent.extras
        var id = data!!.getInt("id")
        var name = data!!.getString("name")
        supportActionBar!!.title = "$name Movies"
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        val apiService: MovieApiInterface = TheTMDBClient.getClient()
        moviePagedListRepository = DiscoverPagedListRepository(apiService)

        discoverViewModel = discoverViewModel(id)

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = discoverAdapter.getItemViewType(position)
                return if (viewType == discoverAdapter.POPULAR_MOVIE_VIEW_TYPE) 1
                else 4
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

    private fun discoverViewModel(id: Int): DiscoverViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return DiscoverViewModel(moviePagedListRepository, id) as T
            }
        })[DiscoverViewModel::class.java]
    }
}