package com.scudderapps.moviesup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import butterknife.BindView
import butterknife.ButterKnife
import com.irfaan008.irbottomnavigation.SpaceItem
import com.irfaan008.irbottomnavigation.SpaceNavigationView
import com.irfaan008.irbottomnavigation.SpaceOnClickListener
import com.scudderapps.moviesup.fragments.MovieFragment
import com.scudderapps.moviesup.fragments.TvFragment

class MainActivity : AppCompatActivity() {

    @BindView(R.id.main_toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.bottom_navigation_bar)
    lateinit var bottomBar: SpaceNavigationView

    lateinit var transaction: FragmentTransaction
    lateinit var movieFragment: MovieFragment
    lateinit var tvFragment: TvFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        bottomBarSetting(savedInstanceState)
        movieFragment = MovieFragment()
        tvFragment = TvFragment()

        setFragment(movieFragment)

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
                        setFragment(movieFragment)
                    }
                    1 -> {
                        setFragment(tvFragment)
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
}
