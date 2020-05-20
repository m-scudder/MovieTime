package com.scudderapps.moviesup.adapter.movie

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.scudderapps.moviesup.fragments.moviedetails.AboutMovieFragment
import com.scudderapps.moviesup.fragments.moviedetails.MovieCastFragment

class MovieDetailTabAdapter(
    private val movieId: Int,
    private val myContext: Context,
    fm: FragmentManager,
    private var totalTabs: Int
) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return AboutMovieFragment(movieId)
            }
            1 -> {
                return MovieCastFragment(movieId)
            }
            2 -> {

            }
        }
        return AboutMovieFragment(movieId)
    }

    override fun getCount(): Int {
        return totalTabs
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "About"
            1 -> "Cast"
            2 -> "Reviews"
            3 -> "Similar"
            4 -> "Recommended"
            else -> null
        }
    }
}