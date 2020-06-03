package com.scudderapps.moviesup.utils

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.GravityCompat
import com.google.android.material.bottomappbar.BottomAppBar
import com.mikepenz.crossfadedrawerlayout.view.CrossfadeDrawerLayout
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.interfaces.ICrossfader
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.util.DrawerUIUtils
import com.scudderapps.moviesup.R

class DrawerLayout(val context: Context, val activity: Activity, val toolbar: BottomAppBar) {

     fun setUpDrawerLayout(savedInstanceState: Bundle?) {

        val tmdbTab: PrimaryDrawerItem = PrimaryDrawerItem()
            .withIcon(R.drawable.tmdb_icon)
            .withName("TMDb")

        val imdbTab: PrimaryDrawerItem = PrimaryDrawerItem()
            .withIcon(R.drawable.imdb_icon)
            .withName("IMDb")

        val homepageTab: PrimaryDrawerItem = PrimaryDrawerItem()
            .withIcon(R.drawable.no_internet)
            .withName("Homepage")

        val wikipediaTab: PrimaryDrawerItem = PrimaryDrawerItem()
            .withIcon(R.drawable.wikipedia_icon)
            .withName("Wikipedia")

        val netflixTab: PrimaryDrawerItem = PrimaryDrawerItem()
            .withIcon(R.drawable.netflix)
            .withName("Netflix")

        val primeTab: PrimaryDrawerItem = PrimaryDrawerItem()
            .withIcon(R.drawable.prime_video_icon)
            .withName("Prime Video")

        val disneyTab: PrimaryDrawerItem = PrimaryDrawerItem()
            .withIcon(R.drawable.disney)
            .withName("Disney +")

        val fbTab: PrimaryDrawerItem = PrimaryDrawerItem()
            .withIcon(R.drawable.fb_icon)
            .withName("Facebook")

        val instagramTab: PrimaryDrawerItem = PrimaryDrawerItem()
            .withIcon(R.drawable.insta_icon)
            .withName("Instagram")

        val twitterTab: PrimaryDrawerItem = PrimaryDrawerItem()
            .withIcon(R.drawable.twitter_icon)
            .withName("Twitter")

        val crossfadeDrawerLayout = CrossfadeDrawerLayout(context)

        var result = DrawerBuilder()
            .withActivity(activity)
            .withToolbar(toolbar)
            .withTranslucentStatusBar(false)
            .withSelectedItem(-1)
            .addDrawerItems(
                SecondaryDrawerItem().withName("Open With").withSelectable(false),
                tmdbTab,
                imdbTab,
                homepageTab,
                wikipediaTab,
                DividerDrawerItem(),
                SecondaryDrawerItem().withName("Stream On").withSelectable(false),
                netflixTab,
                primeTab,
                disneyTab,
                DividerDrawerItem(),
                SecondaryDrawerItem().withName("Social Media").withSelectable(false),
                fbTab,
                instagramTab,
                twitterTab
            )
            .withHasStableIds(true)
            .withSavedInstance(savedInstanceState)
            .withDrawerLayout(crossfadeDrawerLayout)
            .withDrawerWidthDp(72)
            .withGenerateMiniDrawer(true)
            .build()

        crossfadeDrawerLayout.setMaxWidthPx(DrawerUIUtils.getOptimalDrawerWidth(context))

        val miniResult = result.miniDrawer

        val view: View = miniResult.build(activity)
        view.setBackgroundColor(getColor(context, R.color.primary_dark))

        crossfadeDrawerLayout.smallView
            .addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        miniResult.withCrossFader(object : ICrossfader {
            override fun crossfade() {
                crossfadeDrawerLayout.crossfade(400)
                if (isCrossfaded) {
                    result.drawerLayout.closeDrawer(GravityCompat.START)
                }
            }

            override fun isCrossfaded(): Boolean {
                return crossfadeDrawerLayout.isCrossfaded
            }
        })

    }
}