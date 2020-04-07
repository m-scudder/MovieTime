package com.scudderapps.moviesup

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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

    @BindView(R.id.popularMovieList)
    lateinit var popularMovieView: RecyclerView

    @BindView(R.id.trendingMovieList)
    lateinit var trendingMovieView: RecyclerView

    @BindView(R.id.upcomingMovieList)
    lateinit var upcomingMovieView: RecyclerView

    @BindView(R.id.errorTextPopular)
    lateinit var errorTextView: TextView

    @BindView(R.id.popular)
    lateinit var popularTextView: TextView

    @BindView(R.id.trending)
    lateinit var trendingTextView: TextView

    @BindView(R.id.upcoming)
    lateinit var upcomingTextView: TextView

    @BindView(R.id.error_layout)
    lateinit var errorLayout: LinearLayout

    @BindView(R.id.tryAgainBtn)
    lateinit var try_again_btn: Button

    private lateinit var listViewModel: MovieListViewModel
    lateinit var moviePagedListRepository: MoviePagedListRepository
    private val popularAdapter = MoviePageListAdapter(this)
    private val trendingAdapter = MoviePageListAdapter(this)
    private val upcomingAdapter = MoviePageListAdapter(this)
    private val linearLayoutManager = LinearLayoutManager(this)
    private val linearLayoutManager2 = LinearLayoutManager(this)
    private val linearLayoutManager3 = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)

        val apiService: TheTMDBApiInterface = TheTMDBClient.getClient()
        moviePagedListRepository = MoviePagedListRepository(apiService)

        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        linearLayoutManager2.orientation = LinearLayoutManager.HORIZONTAL
        linearLayoutManager3.orientation = LinearLayoutManager.HORIZONTAL

        listViewModel = getViewModel()

        if (isNetworkAvailable()) {
            populatingViews()
        } else {
            try_again_btn.setOnClickListener(View.OnClickListener {
                if (isNetworkAvailable()) {
                    populatingViews()
                } else {
                    Toast.makeText(this, "Still Not Connected", Toast.LENGTH_LONG).show()
                    errorLayout.visibility = View.VISIBLE
                    errorTextView.text = getString(R.string.no_internet_connection)
                    popularTextView.visibility = View.GONE
                    trendingTextView.visibility = View.GONE
                    upcomingTextView.visibility = View.GONE
                }
            })
            errorLayout.visibility = View.VISIBLE
            errorTextView.text = getString(R.string.no_internet_connection)
            popularTextView.visibility = View.GONE
            trendingTextView.visibility = View.GONE
            upcomingTextView.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
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

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun populatingViews() {

        errorLayout.visibility = View.GONE
        listViewModel.popularMoviePagedList.observe(this, Observer {
            popularAdapter.submitList(it)
            popularMovieView.layoutManager = linearLayoutManager
            popularMovieView.setHasFixedSize(true)
            popularMovieView.adapter = popularAdapter
            popularTextView.visibility = View.VISIBLE

        })
        listViewModel.topRatedMoviePagedList.observe(this, Observer {
            trendingAdapter.submitList(it)
            trendingMovieView.layoutManager = linearLayoutManager2
            trendingMovieView.setHasFixedSize(true)
            trendingMovieView.adapter = trendingAdapter
            trendingTextView.visibility = View.VISIBLE
        })

        listViewModel.upcomingMoviePagedList.observe(this, Observer {
            upcomingAdapter.submitList(it)
            upcomingMovieView.layoutManager = linearLayoutManager3
            upcomingMovieView.setHasFixedSize(true)
            upcomingMovieView.adapter = upcomingAdapter
            upcomingTextView.visibility = View.VISIBLE
        })

        listViewModel.networkState.observe(this, Observer {
            if (listViewModel.listIsEmpty() && it == NetworkState.LOADING) {
                popularTextView.visibility = View.GONE
                trendingTextView.visibility = View.GONE
                upcomingTextView.visibility = View.GONE
            } else {
                popularTextView.visibility = View.VISIBLE
                trendingTextView.visibility = View.VISIBLE
                upcomingTextView.visibility = View.VISIBLE
            }
            if (listViewModel.listIsEmpty() && it == NetworkState.ERROR) {
                popularAdapter.setNetworkState(it)
                trendingAdapter.setNetworkState(it)
                upcomingAdapter.setNetworkState(it)
            }
        })
    }
}
