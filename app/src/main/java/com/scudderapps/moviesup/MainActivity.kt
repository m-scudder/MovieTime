package com.scudderapps.moviesup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.irfaan008.irbottomnavigation.SpaceItem
import com.irfaan008.irbottomnavigation.SpaceNavigationView


class MainActivity : AppCompatActivity() {

    @BindView(R.id.main_toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.bottom_navigation_bar)
    lateinit var bottomBar: SpaceNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)

        bottomBar.initWithSaveInstanceState(savedInstanceState)
        bottomBar.addSpaceItem(SpaceItem("Movie", R.drawable.movie))
        bottomBar.addSpaceItem(SpaceItem("Tv", R.drawable.tv))
        bottomBar.addSpaceItem(SpaceItem("Watchlist", R.drawable.watchlist))
        bottomBar.addSpaceItem(SpaceItem("Favorites", R.drawable.fav))
        bottomBar.setCentreButtonIcon(R.drawable.search500)
        bottomBar.setCentreButtonIconColorFilterEnabled(false)
        bottomBar.setCentreButtonColor(resources.getColor(R.color.orange))
    }
}
