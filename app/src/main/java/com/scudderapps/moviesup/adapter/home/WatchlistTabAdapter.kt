package com.scudderapps.moviesup.adapter.home

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.scudderapps.moviesup.fragments.home.MovieListFragment
import com.scudderapps.moviesup.fragments.home.TvListFragment

class WatchlistTabAdapter(
    private val myContext: Context,
    fm: FragmentManager,
    private var totalTabs: Int
) :
    FragmentPagerAdapter(fm!!) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return MovieListFragment(
                    "now_playing"
                )
            }
            1 -> {
                return TvListFragment(
                    "top_rated"
                )
            }
        }
        return TvListFragment(
            "top_rated"
        )
    }

    override fun getCount(): Int {
        return totalTabs
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Movies"
            1 -> "Shows"
            else -> null
        }
    }
}