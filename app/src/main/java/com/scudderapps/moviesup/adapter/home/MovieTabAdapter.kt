package com.scudderapps.moviesup.adapter.home

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.scudderapps.moviesup.fragments.home.MovieListFragment

class MovieTabAdapter(
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
                return MovieListFragment(
                    "popular"
                )
            }
            2 -> {
                return MovieListFragment(
                    "top_rated"
                )
            }
            3 -> {
                return MovieListFragment(
                    "upcoming"
                )
            }
        }
        return MovieListFragment("upcoming")
    }

    override fun getCount(): Int {
        return totalTabs
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Now Playing"
            1 -> "Popular"
            2 -> "Top Rated"
            3 -> "Upcoming"
            else -> null
        }
    }
}