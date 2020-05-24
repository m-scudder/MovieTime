package com.scudderapps.moviesup.adapter.tvshows.tvdetails

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.scudderapps.moviesup.fragments.tvshowsdetails.TvAboutFragment
import com.scudderapps.moviesup.fragments.tvshowsdetails.TvCastFragment
import com.scudderapps.moviesup.fragments.tvshowsdetails.TvCrewFragment
import com.scudderapps.moviesup.fragments.tvshowsdetails.TvSeasonsFragment

class TvDetailTabAdapter(
    private val tvId: Int,
    private val myContext: Context,
    fm: FragmentManager,
    private var totalTabs: Int
) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return TvAboutFragment(tvId)
            }
            1 -> {
                return TvSeasonsFragment(tvId)
            }
            2 -> {
                return TvCastFragment(tvId)
            }
            3 -> {
                return TvCrewFragment(tvId)
            }
        }
        return TvAboutFragment(tvId)
    }

    override fun getCount(): Int {
        return totalTabs
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "About"
            1 -> "Seasons"
            2 -> "Cast"
            3 -> "Crew"
            4 -> "Reviews"
            5 -> "Similar"
            6 -> "Recommended"
            else -> null
        }
    }
}