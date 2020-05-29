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
import com.scudderapps.moviesup.adapter.castandncrew.MovieAdapter
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.api.TmdbApiInterface
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.discover.lists.FeaturedListRepository
import com.scudderapps.moviesup.viewmodel.FeatureViewModel

class FeaturedMovies : AppCompatActivity() {

    @BindView(R.id.featured_movies_view)
    lateinit var topMoviesList: RecyclerView

    @BindView(R.id.top_bar)
    lateinit var topBar: ProgressBar

    @BindView(R.id.top_movie_toolbar)
    lateinit var topToolBar: Toolbar

    private lateinit var featuredListRepository: FeaturedListRepository
    private lateinit var featureViewModel: FeatureViewModel
    private lateinit var topMovieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_featured_movies)
        ButterKnife.bind(this)
        setSupportActionBar(topToolBar)
        supportActionBar!!.title = "Featured Movies List"

        val data = intent.extras
        var listId = data!!.getInt("listId")

        val tmdbApiService: TmdbApiInterface = TheTMDBClient.getClient()
        featuredListRepository = FeaturedListRepository(tmdbApiService)
        featureViewModel = getTopMovie(listId)

        val linearLayoutManager = GridLayoutManager(this, 3)
        featureViewModel.featuredMovieLists.observe(this, Observer {
            val movieList = it.items
            topMovieAdapter = MovieAdapter(movieList, this)
            topMoviesList.layoutManager = linearLayoutManager
            topMoviesList.setHasFixedSize(true)
            topMoviesList.adapter = topMovieAdapter
        })

        featureViewModel.getNetworkState.observe(this, Observer {
            topBar.visibility =
                if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
        })
    }

    private fun getTopMovie(listID: Int): FeatureViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return FeatureViewModel(featuredListRepository, listID) as T
            }
        })[FeatureViewModel::class.java]
    }

}
