package com.scudderapps.moviesup.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.scudderapps.moviesup.fragments.MovieListFragment

class MovieTabAdapter(private val myContext: Context, fm: FragmentManager, private var totalTabs: Int) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            1 -> {
                return MovieListFragment("popular")
            }
            2 -> {
                return MovieListFragment("now_playing")
            }
            3 -> {
                return MovieListFragment("upcoming")
            }
        }
        return MovieListFragment("top_rated")
    }

    override fun getCount(): Int {
        return totalTabs
    }
}