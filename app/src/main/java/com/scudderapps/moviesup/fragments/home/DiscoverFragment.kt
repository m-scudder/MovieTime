package com.scudderapps.moviesup.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.adapter.discover.FeaturedMovieListAdapter
import com.scudderapps.moviesup.adapter.discover.FeaturedTvListAdapter
import com.scudderapps.moviesup.adapter.home.PeoplePagedListAdapter
import com.scudderapps.moviesup.adapter.movie.MovieGenreListAdapter
import com.scudderapps.moviesup.adapter.tvshows.tvdetails.TvGenreListAdapter
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.api.TmdbApiInterface
import com.scudderapps.moviesup.models.featuredlist.FeaturedMovieItem
import com.scudderapps.moviesup.models.featuredlist.FeaturedTvItem
import com.scudderapps.moviesup.repository.discover.PeoplePagedListRepository
import com.scudderapps.moviesup.repository.genre.GenreRepository
import com.scudderapps.moviesup.viewmodel.GenresViewModel
import com.scudderapps.moviesup.viewmodel.PeopleListViewModel


class DiscoverFragment : Fragment() {

    private lateinit var rootView: View

    @BindView(R.id.discover_movie_genre_list)
    lateinit var discoverMovieGenreList: RecyclerView

    @BindView(R.id.discover_tv_genre_list)
    lateinit var discoverTvGenreList: RecyclerView

    @BindView(R.id.discover_people_list)
    lateinit var discoverPeopleList: RecyclerView

    @BindView(R.id.featured_movie_list_view)
    lateinit var featuredMovieListView: RecyclerView

    @BindView(R.id.featured_tv_list_view)
    lateinit var featuredTvListView: RecyclerView

    private lateinit var genresViewModel: GenresViewModel
    private lateinit var genreRepository: GenreRepository
    private lateinit var movieGenreListAdapter: MovieGenreListAdapter
    private lateinit var tvGenreListAdapter: TvGenreListAdapter

    private lateinit var peopleRepository: PeoplePagedListRepository
    private lateinit var peopleViewModel: PeopleListViewModel
    private lateinit var peopleListAdapter: PeoplePagedListAdapter

    private lateinit var featuredMovieListAdapter: FeaturedMovieListAdapter
    private val featuredMovieList = ArrayList<FeaturedMovieItem>()

    private lateinit var featuredTvListAdapter: FeaturedTvListAdapter
    private val featuredTvList = ArrayList<FeaturedTvItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.discover_fragment, container, false)
        Log.d("DiscoverFragment :", "onCreateView Called")
        ButterKnife.bind(this, rootView)
        peopleListAdapter = PeoplePagedListAdapter(context!!.applicationContext)
        val apiService: TmdbApiInterface = TheTMDBClient.getClient()
        genreRepository = GenreRepository(apiService)
        peopleRepository = PeoplePagedListRepository(apiService)

        genresViewModel = getGenreViewModel()
        peopleViewModel = getPeopleViewModel()
        populatingFeatureList()
        populatingViews()
        return rootView
    }

    private fun addingFeaturedMovieList() {
        featuredMovieList.add(
            0,
            FeaturedMovieItem(
                "All time Top Rated Movies(IMDb)",
                "https://image.tmdb.org/t/p/original/avedvodAZUcwqevBfm8p4G2NziQ.jpg",
                144105
            )
        )
        featuredMovieList.add(
            1,
            FeaturedMovieItem(
                "Top 10 Movies 2019 (TMDb)",
                "https://image.tmdb.org/t/p/original/ApiBzeaa95TNYliSbQ8pJv4Fje7.jpg",
                132857
            )
        )
        featuredMovieList.add(
            2,
            FeaturedMovieItem(
                "Golden Globe Winners 2020",
                "https://image.tmdb.org/t/p/original/2lBOQK06tltt8SQaswgb8d657Mv.jpg",
                132860
            )
        )
        featuredMovieList.add(
            3,
            FeaturedMovieItem(
                "Oscar 2019",
                "https://image.tmdb.org/t/p/original/78PjwaykLY2QqhMfWRDvmfbC6EV.jpg",
                118240
            )
        )
        featuredMovieList.add(
            4,
            FeaturedMovieItem(
                "Spy Movies",
                "https://image.tmdb.org/t/p/original/nMgELJEb9ly9HIamnX4ZZ1Z2pH6.jpg",
                82976
            )
        )
    }

    private fun addingFeaturedTvList() {
        featuredTvList.add(
            0,
            FeaturedTvItem(
                "Top 10 Shows 2019 (TMDb)",
                "https://image.tmdb.org/t/p/original/uL6Ad12W09L1sfuOE2pcTeak7bt.jpg",
                132859
            )
        )

        featuredTvList.add(
            1,
            FeaturedTvItem(
                "Fantasy Series",
                "https://image.tmdb.org/t/p/original/s56eyXy8rADp5DpZknfe2HXq4u4.jpg",
                132861
            )
        )

        featuredTvList.add(
            2,
            FeaturedTvItem(
                "Creepy Series",
                "https://image.tmdb.org/t/p/original/56v2KjBlU4XaOv9rVYEQypROD7P.jpg",
                132862
            )
        )

        featuredTvList.add(
            3,
            FeaturedTvItem(
                "Netflix Top Picks",
                "https://image.tmdb.org/t/p/original/qsnXwGS7KBbX4JLqHvICngtR8qg.jpg",
                43923
            )
        )
    }

    private fun populatingFeatureList() {
        addingFeaturedMovieList()
        addingFeaturedTvList()
        val linearLayoutManager4 = LinearLayoutManager(activity)
        linearLayoutManager4.orientation = LinearLayoutManager.HORIZONTAL
        featuredMovieListAdapter = FeaturedMovieListAdapter(featuredMovieList, rootView.context)
        featuredMovieListView.layoutManager = linearLayoutManager4
        featuredMovieListView.setHasFixedSize(true)
        featuredMovieListView.adapter = featuredMovieListAdapter

        val linearLayoutManager5 = LinearLayoutManager(activity)
        linearLayoutManager5.orientation = LinearLayoutManager.HORIZONTAL
        featuredTvListAdapter = FeaturedTvListAdapter(featuredTvList, rootView.context)
        featuredTvListView.layoutManager = linearLayoutManager5
        featuredTvListView.setHasFixedSize(true)
        featuredTvListView.adapter = featuredTvListAdapter
    }

    private fun populatingViews() {
        genresViewModel.movieGenresList.observe(viewLifecycleOwner, Observer {
            movieGenreListAdapter = MovieGenreListAdapter(it.genres, rootView.context)
            val linearLayoutManager = LinearLayoutManager(activity)
            linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            discoverMovieGenreList.layoutManager = linearLayoutManager
            discoverMovieGenreList.setHasFixedSize(true)
            discoverMovieGenreList.adapter = movieGenreListAdapter
        })

        genresViewModel.tvGenresList.observe(viewLifecycleOwner, Observer {
            tvGenreListAdapter = TvGenreListAdapter(it.genres, rootView.context)
            val linearLayoutManager2 = LinearLayoutManager(activity)
            linearLayoutManager2.orientation = LinearLayoutManager.HORIZONTAL
            discoverTvGenreList.layoutManager = linearLayoutManager2
            discoverTvGenreList.setHasFixedSize(true)
            discoverTvGenreList.adapter = tvGenreListAdapter
        })

        peopleViewModel.peoplePagedList.observe(viewLifecycleOwner, Observer {
            peopleListAdapter = PeoplePagedListAdapter(context!!.applicationContext)
            peopleListAdapter.submitList(it)
            val linearLayoutManager3 = LinearLayoutManager(activity)
            linearLayoutManager3.orientation = LinearLayoutManager.HORIZONTAL
            discoverPeopleList.layoutManager = linearLayoutManager3
            discoverPeopleList.setHasFixedSize(true)
            discoverPeopleList.adapter = peopleListAdapter
        })

    }

    private fun getGenreViewModel(): GenresViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return GenresViewModel(genreRepository) as T
            }
        })[GenresViewModel::class.java]
    }

    private fun getPeopleViewModel(): PeopleListViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PeopleListViewModel(peopleRepository) as T
            }
        })[PeopleListViewModel::class.java]
    }

    override fun onResume() {
        Log.d("DiscoverFragment :", "onResume Called")
        populatingViews()
        super.onResume()
    }

    override fun onPause() {
        Log.d("DiscoverFragment :", "onPause Called")
        populatingViews()
        super.onPause()
    }
}
