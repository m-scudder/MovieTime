package com.scudderapps.moviesup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.tabs.TabLayout
import com.irfaan008.irbottomnavigation.SpaceItem
import com.irfaan008.irbottomnavigation.SpaceNavigationView
import com.irfaan008.irbottomnavigation.SpaceOnClickListener
import com.scudderapps.moviesup.adapter.CategoryAdapter
import com.scudderapps.moviesup.adapter.MoviePageListAdapter
import com.scudderapps.moviesup.api.TheTMDBApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.fragments.MovieFragment
import com.scudderapps.moviesup.repository.movielist.MoviePagedListRepository
import com.scudderapps.moviesup.viewmodel.MovieListViewModel


class MainActivity : AppCompatActivity() {

    @BindView(R.id.main_toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.bottom_navigation_bar)
    lateinit var bottomBar: SpaceNavigationView

    @BindView(R.id.viewpager)
    lateinit var viewPager: ViewPager

    @BindView(R.id.tablayout)
    lateinit var tabLayout: TabLayout

    private val fragments = ArrayList<Fragment>()

    @BindView(R.id.moviesList)
    lateinit var movieList: RecyclerView

    private lateinit var listViewModel: MovieListViewModel
    lateinit var moviePagedListRepository: MoviePagedListRepository
    private val popularAdapter = MoviePageListAdapter(this)
    private val linearLayoutManager = GridLayoutManager(this, 3)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        bottomBarSetting(savedInstanceState)

        val apiService: TheTMDBApiInterface = TheTMDBClient.getClient()
        moviePagedListRepository = MoviePagedListRepository(apiService)

        linearLayoutManager.orientation = GridLayoutManager.VERTICAL
        linearLayoutManager.reverseLayout = false

        listViewModel = movieListViewModel()


        fragments.add(MovieFragment())
        fragments.add(MovieFragment())
        fragments.add(MovieFragment())
        fragments.add(MovieFragment())

        val pagerAdapter =
            CategoryAdapter(supportFragmentManager, fragments)
        viewPager.adapter = pagerAdapter

        tabLayout.setupWithViewPager(viewPager)

        tabLayout.getTabAt(0)?.text = "Trending"
        tabLayout.getTabAt(1)?.text = "Popular"
        tabLayout.getTabAt(2)?.text = "Now Playing"
        tabLayout.getTabAt(3)?.text = "Upcoming"

//        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                tabLayout.getTabAt(0)!!.icon!!.setColorFilter(
//                    resources.getColor(android.R.color.black),
//                    PorterDuff.Mode.SRC_IN
//                )
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {
//                tabLayout.getTabAt(2)!!.icon!!.setColorFilter(
//                    resources.getColor(android.R.color.white),
//                    PorterDuff.Mode.SRC_IN
//                )
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab) {}
//        })

    }

    private fun movieListViewModel(): MovieListViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieListViewModel(moviePagedListRepository) as T
            }
        })[MovieListViewModel::class.java]
    }

    private fun bottomBarSetting(savedInstanceState: Bundle?) {
        bottomBar.initWithSaveInstanceState(savedInstanceState)
        bottomBar.addSpaceItem(SpaceItem("Movie", R.drawable.movie))
        bottomBar.addSpaceItem(SpaceItem("Tv", R.drawable.tv))
        bottomBar.addSpaceItem(SpaceItem("Watchlist", R.drawable.watchlist))
        bottomBar.addSpaceItem(SpaceItem("Favorites", R.drawable.fav))
        bottomBar.setCentreButtonIcon(R.drawable.search500)
        bottomBar.setCentreButtonIconColorFilterEnabled(false)
        bottomBar.setCentreButtonColor(resources.getColor(R.color.orange))
        bottomBar.showIconOnly()

        bottomBar.setSpaceOnClickListener(object : SpaceOnClickListener {
            override fun onCentreButtonClick() {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            override fun onItemClick(itemIndex: Int, itemName: String) {
                when (itemIndex) {
                    0 -> {

                    }
                    1 -> {

                    }
                    2 -> {

                    }
                    else -> {

                    }
                }

            }

            override fun onItemReselected(itemIndex: Int, itemName: String) {

            }
        })
    }
}
