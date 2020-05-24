package com.scudderapps.moviesup.adapter.common


import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.scudderapps.moviesup.fragments.cast.CastAboutFragment
import com.scudderapps.moviesup.fragments.cast.CastMovieFragment
import com.scudderapps.moviesup.fragments.cast.CastShowsFragment

class CastDetailTabAdapter(
    private val castId: Int,
    private val myContext: Context,
    fm: FragmentManager,
    private var totalTabs: Int
) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return CastAboutFragment(
                    castId
                )
            }
            1 -> {
                return CastMovieFragment(
                    castId
                )
            }
//            2 -> {
////                return CastShowsFragment(
////                    castId
////                )
////            }
        }
        return CastShowsFragment(
            castId
        )
    }

    override fun getCount(): Int {
        return totalTabs
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "About"
            1 -> "Movies"
            2 -> "Shows"
            else -> null
        }
    }
}