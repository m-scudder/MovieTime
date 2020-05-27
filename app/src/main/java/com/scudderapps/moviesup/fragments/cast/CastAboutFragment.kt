package com.scudderapps.moviesup.fragments.cast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.ms.square.android.expandabletextview.ExpandableTextView
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.adapter.common.AlsoKnownAsAdapter
import com.scudderapps.moviesup.api.TmdbApiInterface
import com.scudderapps.moviesup.api.IMAGE_BASE_URL
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.models.main.PeopleProfileImages
import com.scudderapps.moviesup.repository.cast.CastDetailRepository
import com.scudderapps.moviesup.viewmodel.CastDetailViewModel
import com.stfalcon.imageviewer.StfalconImageViewer
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CastAboutFragment(val castId: Int) : Fragment() {

    private lateinit var rootView: View

    @BindView(R.id.also_known_as_view)
    lateinit var alsoKnowAsView: RecyclerView

    @BindView(R.id.people_bio)
    lateinit var peopleBiography: ExpandableTextView

    @BindView(R.id.people_birthdate)
    lateinit var peopleBirthdate: TextView

    @BindView(R.id.people_birthplace)
    lateinit var peopleBirthplace: TextView

    @BindView(R.id.people_media)
    lateinit var peopleMedia: ImageView

    @BindView(R.id.people_count)
    lateinit var peopleCount: TextView

    @BindView(R.id.knonAs_layout)
    lateinit var knownAsLayout: LinearLayout

    @BindView(R.id.bio_layout)
    lateinit var bioLayout: LinearLayout

    @BindView(R.id.images_layout)
    lateinit var imagesLayout: LinearLayout

    lateinit var peopleDetailRepository: CastDetailRepository
    private lateinit var peopleDetailViewModel: CastDetailViewModel
    lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var alsoKnownAsAdapter: AlsoKnownAsAdapter
    private lateinit var peopleProfiles: List<PeopleProfileImages>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.cast_about_fragment, container, false)
        ButterKnife.bind(this, rootView)

        val apiService: TmdbApiInterface = TheTMDBClient.getClient()
        peopleDetailRepository =
            CastDetailRepository(
                apiService
            )
        peopleDetailViewModel = getViewModel(castId)

        peopleDetailViewModel.peopleDetails.observe(viewLifecycleOwner, Observer {
            if (!it.biography.isNullOrEmpty()) {
                peopleBiography.text = it.biography
            } else {
                bioLayout.visibility = View.GONE
            }
            if (!it.alsoKnownAs.isNullOrEmpty()) {
                alsoKnownAsAdapter =
                    AlsoKnownAsAdapter(
                        it.alsoKnownAs,
                        rootView.context
                    )
                linearLayoutManager = LinearLayoutManager(activity)
                linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                alsoKnowAsView.layoutManager = linearLayoutManager
                alsoKnowAsView.setHasFixedSize(true)
                alsoKnowAsView.adapter = alsoKnownAsAdapter
            } else {
                knownAsLayout.visibility = View.GONE
            }

            if (!it.birthday.isNullOrEmpty()) {
                val originalFormat: DateFormat = SimpleDateFormat("yyyy-MM-d", Locale.ENGLISH)
                val targetFormat: DateFormat = SimpleDateFormat(getString(R.string.dateFormat))
                val date: Date = originalFormat.parse(it.birthday)
                val formattedDate: String = targetFormat.format(date)
                peopleBirthdate.text = formattedDate
            } else {
                peopleBirthdate.text = "-"
            }
            if (!it.placeOfBirth.isNullOrEmpty()) {
                peopleBirthplace.text = it.placeOfBirth
            } else {
                peopleBirthplace.text = "-"
            }
        })

        peopleDetailViewModel.peopleImages.observe(viewLifecycleOwner, Observer {
            peopleProfiles = it.profiles
            if (!peopleProfiles.isNullOrEmpty()) {
                imagesLayout.visibility = View.VISIBLE
                var mediaPosterURL = peopleProfiles[0].filePath
                val mediaPosterUrl: String = IMAGE_BASE_URL + mediaPosterURL
                Glide.with(this).load(mediaPosterUrl).into(peopleMedia)
                if (peopleProfiles.size == 1) {
                    peopleCount.text = peopleProfiles.size.toString() + " Image"
                } else {
                    peopleCount.text = peopleProfiles.size.toString() + " Images"
                }
                peopleMedia.setOnClickListener(View.OnClickListener {
                    StfalconImageViewer.Builder(
                        activity,
                        peopleProfiles
                    ) { posterMedia: ImageView, profile: PeopleProfileImages ->
                        Glide.with(this).load(IMAGE_BASE_URL + profile.filePath).into(posterMedia)
                    }
                        .withHiddenStatusBar(false)
                        .show()
                })
            } else {
                Glide.with(this).load(R.drawable.no_image_found).centerInside().into(peopleMedia)
                peopleCount.text = "No Images"
            }
        })

        return rootView
    }

    private fun getViewModel(castId: Int): CastDetailViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CastDetailViewModel(peopleDetailRepository, castId) as T
            }
        })[CastDetailViewModel::class.java]
    }
}
