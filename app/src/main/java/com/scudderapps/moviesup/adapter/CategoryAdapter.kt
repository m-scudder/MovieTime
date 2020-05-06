package com.scudderapps.moviesup.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class CategoryAdapter(fm: FragmentManager, private val fragment: ArrayList<Fragment>) :
    FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        return fragment[position]
    }

    override fun getCount(): Int {
        return fragment.size
    }
}