package com.scudderapps.moviesup

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.scudderapps.moviesup.adapter.MoviePageListAdapter
import com.scudderapps.moviesup.adapter.PeoplePagedListAdapter
import com.scudderapps.moviesup.api.TheTMDBApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.movielist.MoviePagedListRepository
import com.scudderapps.moviesup.repository.people.PeoplePagedListRepository
import com.scudderapps.moviesup.viewmodel.MovieListViewModel
import com.scudderapps.moviesup.viewmodel.PeopleListViewModel

class MainActivity : AppCompatActivity() {

    @BindView(R.id.main_toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.popularMovieList)
    lateinit var popularMovieView: RecyclerView

    @BindView(R.id.trendingMovieList)
    lateinit var trendingMovieView: RecyclerView

    @BindView(R.id.upcomingMovieList)
    lateinit var upcomingMovieView: RecyclerView

    @BindView(R.id.peopleListView)
    lateinit var peopleListView: RecyclerView

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
    lateinit var try_again_btn: TextView

    @BindView(R.id.movie_layout)
    lateinit var movieLayout: LinearLayout

    @BindView(R.id.searchFab)
    lateinit var searchFabBtn: FloatingActionButton

    private lateinit var listViewModel: MovieListViewModel
    private lateinit var peopleViewModel: PeopleListViewModel
    lateinit var moviePagedListRepository: MoviePagedListRepository
    lateinit var peoplePagedListRepository: PeoplePagedListRepository
    private val popularAdapter = MoviePageListAdapter(this)
    private val trendingAdapter = MoviePageListAdapter(this)
    private val upcomingAdapter = MoviePageListAdapter(this)
    private val peopleAdapter = PeoplePagedListAdapter(this)
    private val linearLayoutManager = LinearLayoutManager(this)
    private val linearLayoutManager2 = LinearLayoutManager(this)
    private val linearLayoutManager3 = LinearLayoutManager(this)
    private val linearLayoutManager4 = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)

        val apiService: TheTMDBApiInterface = TheTMDBClient.getClient()
        moviePagedListRepository = MoviePagedListRepository(apiService)
        peoplePagedListRepository = PeoplePagedListRepository(apiService)

        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        linearLayoutManager.reverseLayout = false
        linearLayoutManager2.orientation = LinearLayoutManager.HORIZONTAL
        linearLayoutManager2.reverseLayout = false
        linearLayoutManager3.orientation = LinearLayoutManager.HORIZONTAL
        linearLayoutManager3.reverseLayout = false
        linearLayoutManager4.orientation = LinearLayoutManager.HORIZONTAL
        linearLayoutManager4.reverseLayout = false

        listViewModel = getViewModel()
        peopleViewModel = getViewMode2()

        if (isNetworkAvailable()) {
            populatingViews()
        } else {
            try_again_btn.setOnClickListener(View.OnClickListener {
                if (isNetworkAvailable()) {
                    populatingViews()
                } else {
                    Toast.makeText(this, "Still Not Connected", Toast.LENGTH_LONG).show()
                    errorLayout.visibility = View.VISIBLE
                    movieLayout.visibility = View.GONE
                    errorTextView.text = getString(R.string.no_internet_connection)

                }
            })
            errorLayout.visibility = View.VISIBLE
            errorTextView.text = getString(R.string.no_internet_connection)
            movieLayout.visibility = View.GONE
        }

        searchFabBtn.setOnClickListener(View.OnClickListener {
            intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
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

    private fun getViewMode2(): PeopleListViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PeopleListViewModel(peoplePagedListRepository) as T
            }
        })[PeopleListViewModel::class.java]
    }


    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun populatingViews() {

        errorLayout.visibility = View.GONE
        movieLayout.visibility = View.VISIBLE
        listViewModel.popularMoviePagedList.observe(this, Observer {
            popularAdapter.submitList(it)
            popularMovieView.layoutManager = linearLayoutManager
            popularMovieView.setHasFixedSize(true)
            popularMovieView.adapter = popularAdapter

        })
        listViewModel.topRatedMoviePagedList.observe(this, Observer {
            trendingAdapter.submitList(it)
            trendingMovieView.layoutManager = linearLayoutManager2
            trendingMovieView.setHasFixedSize(true)
            trendingMovieView.adapter = trendingAdapter
        })

        listViewModel.upcomingMoviePagedList.observe(this, Observer {
            upcomingAdapter.submitList(it)
            upcomingMovieView.layoutManager = linearLayoutManager3
            upcomingMovieView.setHasFixedSize(true)
            upcomingMovieView.adapter = upcomingAdapter
        })

        listViewModel.networkState.observe(this, Observer {
            if (listViewModel.listIsEmpty() && it == NetworkState.LOADING || it == NetworkState.LOADED) {
                popularAdapter.setNetworkState(it)
                trendingAdapter.setNetworkState(it)
                upcomingAdapter.setNetworkState(it)
            }
        })

        peopleViewModel.popularPeoplePagedList.observe(this, Observer {
            peopleAdapter.submitList(it)
            peopleListView.layoutManager = linearLayoutManager4
            peopleListView.setHasFixedSize(true)
            peopleListView.adapter = peopleAdapter
        })

        peopleViewModel.networkState.observe(this, Observer {
            if (peopleViewModel.listIsEmpty() && it == NetworkState.LOADING || it == NetworkState.LOADED) {
                peopleAdapter.setNetworkState(it)
            }
        })
    }

}
