package com.scudderapps.moviesup.adapter.tvshows

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.scudderapps.moviesup.fragments.tvshowsdetails.SeasonEpisodeFragment

class SeasonDetailTabAdapter(
    private val seasonNumber: Int,
    private val tvId: Int,
    private val myContext: Context,
    fm: FragmentManager,
    private var totalTabs: Int
) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return SeasonEpisodeFragment(tvId, seasonNumber)
            }
            1 -> {
            }
            2 -> {
            }
        }
        return SeasonEpisodeFragment(tvId, seasonNumber)
    }

    override fun getCount(): Int {
        return totalTabs
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Episodes"
            1 -> "About"
            2 -> "Cast"
            else -> null
        }
    }
}