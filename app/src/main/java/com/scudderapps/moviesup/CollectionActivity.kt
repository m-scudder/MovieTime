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
import com.scudderapps.moviesup.adapter.movie.MovieAdapter
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.movie.moviedetails.MovieDetailRepository
import com.scudderapps.moviesup.viewmodel.MovieDetailViewModel

class CollectionActivity : AppCompatActivity() {

    @BindView(R.id.collection_progress_bar)
    lateinit var collectionBar: ProgressBar

    @BindView(R.id.collection_view)
    lateinit var collectionView: RecyclerView

    @BindView(R.id.collection_toolbar)
    lateinit var collectionToolbar: Toolbar

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var movieRepository: MovieDetailRepository
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)
        ButterKnife.bind(this)
        setSupportActionBar(collectionToolbar)
        supportActionBar!!.title = "Collection"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val data = intent.extras
        var id = data!!.getInt("id")

        val linearLayoutManager = GridLayoutManager(this, 4)
        val apiService: ApiInterface = TheTMDBClient.getClient()

        movieRepository = MovieDetailRepository(apiService)
        viewModel = getViewModel(id)

        viewModel.collection.observe(this, Observer {
            val movieList = it.parts
            movieAdapter =
                MovieAdapter(
                    movieList,
                    this
                )
            collectionView.layoutManager = linearLayoutManager
            collectionView.setHasFixedSize(true)
            collectionView.adapter = movieAdapter
        })

        viewModel.networkState.observe(this, Observer {
            collectionBar.visibility =
                if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
        })
    }

    private fun getViewModel(id: Int): MovieDetailViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieDetailViewModel(movieRepository, id) as T
            }
        })[MovieDetailViewModel::class.java]
    }

}