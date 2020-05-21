package com.scudderapps.moviesup.adapter.home

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.scudderapps.moviesup.fragments.home.TvListFragment

class TvTabAdapter(
    private val myContext: Context,
    fm: FragmentManager,
    private var totalTabs: Int
) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return TvListFragment("airing_today")
            }
            1 -> {
                return TvListFragment("on_the_air")
            }
            2-> {
                return TvListFragment("popular")
            }
        }
        return TvListFragment("popular")
    }

    override fun getCount(): Int {
        return totalTabs
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Now Playing"
            1 -> "On Air Today"
            2 ->  "Popular"
            else -> null
        }
    }
}