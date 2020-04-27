package com.scudderapps.moviesup

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.analytics.FirebaseAnalytics
import com.scudderapps.moviesup.adapter.GenreListAdapter
import com.scudderapps.moviesup.adapter.MoviePageListAdapter
import com.scudderapps.moviesup.adapter.PeoplePagedListAdapter
import com.scudderapps.moviesup.api.TheTMDBApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.genre.GenreRepository
import com.scudderapps.moviesup.repository.movielist.MoviePagedListRepository
import com.scudderapps.moviesup.repository.peoplelist.PeoplePagedListRepository
import com.scudderapps.moviesup.repository.trending.TrendingPagedListRepository
import com.scudderapps.moviesup.viewmodel.GenresViewModel
import com.scudderapps.moviesup.viewmodel.MovieListViewModel
import com.scudderapps.moviesup.viewmodel.PeopleListViewModel
import com.scudderapps.moviesup.viewmodel.TrendingViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    @BindView(R.id.main_toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.popularMovieList)
    lateinit var popularMovieView: RecyclerView

    @BindView(R.id.nowPlayingMovieList)
    lateinit var nowPlayingMovieView: RecyclerView

    @BindView(R.id.upcomingMovieList)
    lateinit var upcomingMovieView: RecyclerView

    @BindView(R.id.genres_view)
    lateinit var genresView: RecyclerView

    @BindView(R.id.peopleListView)
    lateinit var peopleListView: RecyclerView

    @BindView(R.id.movie_layout)
    lateinit var movieLayout: LinearLayout

    @BindView(R.id.error_layout)
    lateinit var errorLayout: LinearLayout

    @BindView(R.id.errorTextPopular)
    lateinit var errorTextView: TextView

    @BindView(R.id.tryAgainBtn)
    lateinit var try_again_btn: TextView

    @BindView(R.id.searchFab)
    lateinit var searchFabBtn: FloatingActionButton

    @BindView(R.id.main_progress_bar)
    lateinit var mainProgressBar: ProgressBar

    @BindView(R.id.now_playing_bar)
    lateinit var nowPlayingBar: ProgressBar

    @BindView(R.id.popular_bar)
    lateinit var popularBar: ProgressBar

    @BindView(R.id.upcoming_bar)
    lateinit var upcomingBar: ProgressBar

    @BindView(R.id.people_bar)
    lateinit var peopleBar: ProgressBar

    @BindView(R.id.trending_list)
    lateinit var trendingList: RecyclerView

    @BindView(R.id.trending_bar)
    lateinit var trendingBar: ProgressBar

    private lateinit var listViewModel: MovieListViewModel
    private lateinit var peopleViewModel: PeopleListViewModel
    private lateinit var genresViewModel: GenresViewModel
    private lateinit var trendingViewModel: TrendingViewModel
    lateinit var moviePagedListRepository: MoviePagedListRepository
    lateinit var peoplePagedListRepository: PeoplePagedListRepository
    lateinit var trendingPagedListRepository: TrendingPagedListRepository
    lateinit var genreRepository: GenreRepository
    private val popularAdapter = MoviePageListAdapter(this)
    private val nowPlayingAdapter = MoviePageListAdapter(this)
    private val upcomingAdapter = MoviePageListAdapter(this)
    private val peopleAdapter = PeoplePagedListAdapter(this)
    private val trendingAdapter = MoviePageListAdapter(this)
    private lateinit var genreAdapter: GenreListAdapter
    private val linearLayoutManager = LinearLayoutManager(this)
    private val linearLayoutManager2 = LinearLayoutManager(this)
    private val linearLayoutManager3 = LinearLayoutManager(this)
    private val linearLayoutManager4 = LinearLayoutManager(this)
    private val linearLayoutManager5 = LinearLayoutManager(this)
    private val linearLayoutManager6 = LinearLayoutManager(this)

    val REQUEST_CODE = 100;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "AppOpened")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
            == PackageManager.PERMISSION_DENIED
        ) {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, REQUEST_CODE.toString())
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "permission Denied")
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)
            // Requesting the permission
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.INTERNET),
                REQUEST_CODE
            );
        } else {
        }

        val apiService: TheTMDBApiInterface = TheTMDBClient.getClient()
        moviePagedListRepository = MoviePagedListRepository(apiService)
        peoplePagedListRepository = PeoplePagedListRepository(apiService)
        trendingPagedListRepository = TrendingPagedListRepository(apiService)
        genreRepository = GenreRepository(apiService)

        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        linearLayoutManager.reverseLayout = false
        linearLayoutManager2.orientation = LinearLayoutManager.HORIZONTAL
        linearLayoutManager2.reverseLayout = false
        linearLayoutManager3.orientation = LinearLayoutManager.HORIZONTAL
        linearLayoutManager3.reverseLayout = false
        linearLayoutManager4.orientation = LinearLayoutManager.HORIZONTAL
        linearLayoutManager4.reverseLayout = false
        linearLayoutManager5.orientation = LinearLayoutManager.HORIZONTAL
        linearLayoutManager5.reverseLayout = false
        linearLayoutManager6.orientation = LinearLayoutManager.HORIZONTAL
        linearLayoutManager6.reverseLayout = false

        listViewModel = movieListViewModel()
        peopleViewModel = peopleListViewModel()
        genresViewModel = genresListViewModel()

        trendingViewModel = trendingListViewModel("day")

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
            searchFabBtn.visibility = View.GONE
        }

        searchFabBtn.setOnClickListener(View.OnClickListener {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, REQUEST_CODE.toString())
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Click")
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle)
            intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        })
    }

    private fun movieListViewModel(): MovieListViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieListViewModel(moviePagedListRepository) as T
            }
        })[MovieListViewModel::class.java]
    }

    private fun peopleListViewModel(): PeopleListViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PeopleListViewModel(peoplePagedListRepository) as T
            }
        })[PeopleListViewModel::class.java]
    }

    private fun genresListViewModel(): GenresViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return GenresViewModel(genreRepository) as T
            }
        })[GenresViewModel::class.java]
    }

    private fun trendingListViewModel(type: String): TrendingViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return TrendingViewModel(trendingPagedListRepository, type) as T
            }
        })[TrendingViewModel::class.java]
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
        searchFabBtn.visibility = View.VISIBLE

        trendingViewModel.trendingList.observe(this, Observer {
            trendingAdapter.submitList(it)
            trendingList.layoutManager = linearLayoutManager6
            trendingList.setHasFixedSize(true)
            trendingList.adapter = trendingAdapter
        })


        trendingViewModel.networkState.observe(this, Observer {
            trendingBar.visibility =
                if (trendingViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            if (!trendingViewModel.listIsEmpty()) {
                upcomingAdapter.setNetworkState(it)
            }
        })

        listViewModel.popularMoviePagedList.observe(this, Observer {
            popularAdapter.submitList(it)
            popularMovieView.layoutManager = linearLayoutManager
            popularMovieView.setHasFixedSize(true)
            popularMovieView.adapter = popularAdapter

        })
        listViewModel.nowPlayingMoviePagedList.observe(this, Observer {
            nowPlayingAdapter.submitList(it)
            nowPlayingMovieView.layoutManager = linearLayoutManager2
            nowPlayingMovieView.setHasFixedSize(true)
            nowPlayingMovieView.adapter = nowPlayingAdapter
        })

        listViewModel.upcomingMoviePagedList.observe(this, Observer {
            upcomingAdapter.submitList(it)
            upcomingMovieView.layoutManager = linearLayoutManager3
            upcomingMovieView.setHasFixedSize(true)
            upcomingMovieView.adapter = upcomingAdapter
        })
        listViewModel.networkState.observe(this, Observer {

            nowPlayingBar.visibility =
                if (listViewModel.nowPlayingListIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            if (!listViewModel.nowPlayingListIsEmpty()) {
                nowPlayingAdapter.setNetworkState(it)
            }

            popularBar.visibility =
                if (listViewModel.popularListIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            if (!listViewModel.popularListIsEmpty()) {
                popularAdapter.setNetworkState(it)
            }

            upcomingBar.visibility =
                if (listViewModel.upcomingListIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            if (!listViewModel.upcomingListIsEmpty()) {
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
            peopleBar.visibility =
                if (peopleViewModel.peopleListIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            if (!peopleViewModel.peopleListIsEmpty()) {
                peopleAdapter.setNetworkState(it)
            }
        })

        genresViewModel.genresList.observe(this, Observer {
            genreAdapter = GenreListAdapter(it.genres, this)
            genresView.layoutManager = linearLayoutManager5
            genresView.setHasFixedSize(true)
            genresView.adapter = genreAdapter
        })
    }

}
