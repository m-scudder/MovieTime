package com.scudderapps.moviesup

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import butterknife.BindView
import butterknife.ButterKnife
import com.irfaan008.irbottomnavigation.SpaceItem
import com.irfaan008.irbottomnavigation.SpaceNavigationView
import com.irfaan008.irbottomnavigation.SpaceOnClickListener
import com.scudderapps.moviesup.fragments.home.ErrorFragment
import com.scudderapps.moviesup.fragments.home.MovieFragment
import com.scudderapps.moviesup.fragments.home.SearchFragment
import com.scudderapps.moviesup.fragments.home.TvFragment

class MainActivity : AppCompatActivity() {

    @BindView(R.id.main_toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.bottom_navigation_bar)
    lateinit var bottomBar: SpaceNavigationView

    lateinit var transaction: FragmentTransaction
    lateinit var movieFragment: MovieFragment
    lateinit var tvFragment: TvFragment
    lateinit var searchFragment: SearchFragment
    lateinit var errorFragment: ErrorFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        bottomBarSetting(savedInstanceState)
        movieFragment = MovieFragment()
        tvFragment = TvFragment()
        searchFragment = SearchFragment()
        errorFragment = ErrorFragment()

        if (isNetworkAvailable()) {
            setFragment(movieFragment)
        } else {
            setFragment(errorFragment)
        }

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
        bottomBar.setInActiveSpaceItemColor(ContextCompat.getColor(this, R.color.colorAccent));
        bottomBar.setActiveSpaceItemColor(ContextCompat.getColor(this, R.color.orange));

        bottomBar.setSpaceOnClickListener(object : SpaceOnClickListener {
            override fun onCentreButtonClick() {
                setFragment(searchFragment)
            }

            override fun onItemClick(itemIndex: Int, itemName: String) {
                when (itemIndex) {
                    0 -> {
                        if (isNetworkAvailable()) {
                            setFragment(movieFragment)
                        } else {
                            setFragment(errorFragment)
                        }
                    }
                    1 -> {
                        if (isNetworkAvailable()) {
                            setFragment(tvFragment)
                        } else {
                            setFragment(errorFragment)
                        }
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

    private fun setFragment(fragment: Fragment) {
        transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.tab_container, fragment)
        transaction.commit()

    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}
