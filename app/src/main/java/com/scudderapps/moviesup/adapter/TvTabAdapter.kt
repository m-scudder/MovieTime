package com.scudderapps.moviesup.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.scudderapps.moviesup.fragments.TvListFragment

class TvTabAdapter(
    private val myContext: Context,
    fm: FragmentManager,
    private var totalTabs: Int
) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            1 -> {
                return TvListFragment("airing_today")
            }
            2 -> {
                return TvListFragment("on_the_air")
            }
        }
        return TvListFragment("popular")
    }

    override fun getCount(): Int {
        return totalTabs
    }
}