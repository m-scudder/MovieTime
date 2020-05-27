package com.scudderapps.moviesup

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.scudderapps.moviesup.adapter.common.CastDetailTabAdapter
import com.scudderapps.moviesup.api.TmdbApiInterface
import com.scudderapps.moviesup.api.IMAGE_BASE_URL
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.models.main.PeopleDetails
import com.scudderapps.moviesup.repository.cast.CastDetailRepository
import com.scudderapps.moviesup.viewmodel.CastDetailViewModel
import de.hdodenhof.circleimageview.CircleImageView

class CastAndCrewDetailActivity : AppCompatActivity() {

    @BindView(R.id.people_name)
    lateinit var peopleName: TextView

    @BindView(R.id.castImage)
    lateinit var peopleImage: CircleImageView

    @BindView(R.id.people_department)
    lateinit var peopleDepartment: TextView

    @BindView(R.id.people_toolbar)
    lateinit var peopleToolbar: Toolbar

    lateinit var peopleDetailRepository: CastDetailRepository
    private lateinit var peopleDetailViewModel: CastDetailViewModel

    @BindView(R.id.cast_detail_tab_layout)
    lateinit var castDetailTabLayout: TabLayout

    @BindView(R.id.cast_detail_viewpager)
    lateinit var castDetailViewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cast_n_crew_detail)
        ButterKnife.bind(this)

        setSupportActionBar(peopleToolbar)
        supportActionBar!!.title = ("")

        val peopleDetails = intent.extras
        val id: Int = peopleDetails!!.getInt("id")

        val apiService: TmdbApiInterface = TheTMDBClient.getClient()
        peopleDetailRepository =
            CastDetailRepository(
                apiService
            )
        peopleDetailViewModel = getViewMode(id)

        castDetailTabLayout.addTab(castDetailTabLayout.newTab().setText("About"))
        castDetailTabLayout.addTab(castDetailTabLayout.newTab().setText("Movies"))
        castDetailTabLayout.addTab(castDetailTabLayout.newTab().setText("Shows"))

        val adapter =
            CastDetailTabAdapter(
                id,
                this,
                supportFragmentManager,
                castDetailTabLayout.tabCount
            )
        castDetailViewPager.adapter = adapter

        castDetailTabLayout.setupWithViewPager(castDetailViewPager)
        castDetailViewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                castDetailTabLayout
            )
        )
        peopleDetailViewModel.peopleDetails.observe(this, Observer {
            bindUI(it)
        })
    }

    fun bindUI(it: PeopleDetails) {
        peopleName.text = it.name

        peopleDepartment.text = it.knownForDepartment
        var profilePath = it.profilePath
        if (!profilePath.isNullOrEmpty()) {
            val finalPath = IMAGE_BASE_URL + profilePath
            Glide.with(this).load(finalPath).into(peopleImage)
        } else {
            Glide.with(this).load(R.drawable.no_image_found).into(peopleImage)
        }
    }

    private fun getViewMode(peopleID: Int): CastDetailViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CastDetailViewModel(peopleDetailRepository, peopleID) as T
            }
        })[CastDetailViewModel::class.java]
    }
}
