package com.scudderapps.moviesup

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import butterknife.BindView
import butterknife.ButterKnife
import com.gauravk.bubblenavigation.BubbleNavigationConstraintView
import com.scudderapps.moviesup.fragments.home.*

class MainActivity : AppCompatActivity() {

    @BindView(R.id.main_toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.top_navigation_constraint)
    lateinit var bottomBar: BubbleNavigationConstraintView

    lateinit var transaction: FragmentTransaction
    lateinit var movieFragment: MovieFragment
    lateinit var tvFragment: TvFragment
    lateinit var searchFragment: SearchFragment
    lateinit var errorFragment: ErrorFragment
    lateinit var discoverFragment: DiscoverFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
//        bottomBarSetting(savedInstanceState)
        movieFragment = MovieFragment()
        tvFragment = TvFragment()
        searchFragment = SearchFragment()
        errorFragment = ErrorFragment()
        discoverFragment = DiscoverFragment()

        if (isNetworkAvailable()) {
            setFragment(movieFragment)
        } else {
            setFragment(errorFragment)
        }

        bottomBar.setNavigationChangeListener { view, position ->
            when (position) {
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
                    if (isNetworkAvailable()) {
                        setFragment(discoverFragment)
                    } else {
                        setFragment(errorFragment)
                    }
                }
                3 -> {
                    if (isNetworkAvailable()) {
//                        setFragment(searchFragment)
                    } else {
                        setFragment(errorFragment)
                    }
                }
                4 -> {
                    if (isNetworkAvailable()) {
                        setFragment(searchFragment)
                    } else {
                        setFragment(errorFragment)
                    }
                }
                else -> {

                }
            }
        }


    }

//    private fun bottomBarSetting(savedInstanceState: Bundle?) {
//        bottomBar.initWithSaveInstanceState(savedInstanceState)
//        bottomBar.addSpaceItem(SpaceItem("Movie", R.drawable.movie))
//        bottomBar.addSpaceItem(SpaceItem("Tv", R.drawable.tv))
//        bottomBar.addSpaceItem(SpaceItem("Explore", R.drawable.discover))
//        bottomBar.addSpaceItem(SpaceItem("Watchlist", R.drawable.watchlist))
//        bottomBar.setCentreButtonIcon(R.drawable.search500)
//        bottomBar.setCentreButtonIconColorFilterEnabled(false)
//        bottomBar.setCentreButtonColor(resources.getColor(R.color.orange))
//        bottomBar.showIconOnly()
//        bottomBar.setInActiveSpaceItemColor(ContextCompat.getColor(this, R.color.colorAccent));
//        bottomBar.setActiveSpaceItemColor(ContextCompat.getColor(this, R.color.orange));
//
//        bottomBar.setSpaceOnClickListener(object : SpaceOnClickListener {
//            override fun onCentreButtonClick() {
//                setFragment(searchFragment)
//            }
//
//            override fun onItemClick(itemIndex: Int, itemName: String) {
//                when (itemIndex) {
//                    0 -> {
//                        if (isNetworkAvailable()) {
//                            setFragment(movieFragment)
//                        } else {
//                            setFragment(errorFragment)
//                        }
//                    }
//                    1 -> {
//                        if (isNetworkAvailable()) {
//                            setFragment(tvFragment)
//                        } else {
//                            setFragment(errorFragment)
//                        }
//                    }
//                    2 -> {
//                        if (isNetworkAvailable()) {
//                            setFragment(discoverFragment)
//                        } else {
//                            setFragment(errorFragment)
//                        }
//                    }
//                    else -> {
//
//                    }
//                }
//
//            }
//
//            override fun onItemReselected(itemIndex: Int, itemName: String) {
//
//            }
//        })
//    }

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
