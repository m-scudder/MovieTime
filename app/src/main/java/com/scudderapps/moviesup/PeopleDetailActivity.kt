package com.scudderapps.moviesup

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
import com.scudderapps.moviesup.adapter.MovieAdapter
import com.scudderapps.moviesup.adapter.TextViewAdapter
import com.scudderapps.moviesup.api.IMAGE_BASE_URL
import com.scudderapps.moviesup.api.TheTMDBApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.models.PeopleDetails
import com.scudderapps.moviesup.models.PeopleProfileImages
import com.scudderapps.moviesup.repository.peopledetails.PeopleDetailRepository
import com.scudderapps.moviesup.viewmodel.PeopleDetailViewModel
import com.stfalcon.imageviewer.StfalconImageViewer
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PeopleDetailActivity : AppCompatActivity() {

    @BindView(R.id.people_name)
    lateinit var peopleName: TextView

    @BindView(R.id.people_image)
    lateinit var peopleImage: ImageView

    @BindView(R.id.people_department)
    lateinit var peopleDepartment: TextView

    @BindView(R.id.people_birthdate)
    lateinit var peopleBirthdate: TextView

    @BindView(R.id.people_birthplace)
    lateinit var peopleBirthplace: TextView

    @BindView(R.id.also_known_as_view)
    lateinit var alsoKnowAsView: RecyclerView

    @BindView(R.id.movie_credits_list)
    lateinit var movieCreditList: RecyclerView

    @BindView(R.id.people_bio)
    lateinit var peopleBiography: ExpandableTextView

    @BindView(R.id.people_toolbar)
    lateinit var peopleToolbar: Toolbar

    @BindView(R.id.people_media)
    lateinit var peopleMedia: ImageView

    @BindView(R.id.people_count)
    lateinit var peopleCount: TextView

    @BindView(R.id.images_layout)
    lateinit var imagesLayout: LinearLayout

    @BindView(R.id.movie_credit_layout)
    lateinit var movieCreditLayout: LinearLayout

    @BindView(R.id.knonAs_layout)
    lateinit var knownAsLayout: LinearLayout

    @BindView(R.id.bio_layout)
    lateinit var bioLayout: LinearLayout


    lateinit var peopleDetailRepository: PeopleDetailRepository
    private lateinit var peopleDetailViewModel: PeopleDetailViewModel
    lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var textViewAdapter: TextViewAdapter
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var linearLayoutManager2: LinearLayoutManager
    private lateinit var peopleProfiles: List<PeopleProfileImages>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.people_detail_activity)
        ButterKnife.bind(this)

        setSupportActionBar(peopleToolbar)
        supportActionBar!!.title = ("")

        val peopleDetails = intent.extras
        val id: Int = peopleDetails!!.getInt("id")

        linearLayoutManager2 = LinearLayoutManager(this)
        linearLayoutManager2.reverseLayout = false
        linearLayoutManager2.orientation = LinearLayoutManager.HORIZONTAL

        val apiService: TheTMDBApiInterface = TheTMDBClient.getClient()
        peopleDetailRepository = PeopleDetailRepository(apiService)
        peopleDetailViewModel = getViewMode(id)

        peopleDetailViewModel.peopleDetails.observe(this, Observer {
            bindUI(it)
        })
        peopleDetailViewModel.movieCredits.observe(this, Observer {

            if (!it.cast.isNullOrEmpty()) {
                movieAdapter = MovieAdapter(it.cast, this)
                movieCreditList.layoutManager = linearLayoutManager2
                movieCreditList.setHasFixedSize(true)
                movieCreditList.adapter = movieAdapter
            } else {
                movieCreditLayout.visibility = View.GONE
            }
        })
        peopleDetailViewModel.peopleImages.observe(this, Observer {
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
                        this,
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

    }

    fun bindUI(it: PeopleDetails) {

        if (!it.birthday.isNullOrEmpty()) {
            val originalFormat: DateFormat = SimpleDateFormat("yyyy-MM-d", Locale.ENGLISH)
            val targetFormat: DateFormat = SimpleDateFormat(getString(R.string.dateFormat))
            val date: Date = originalFormat.parse(it.birthday)
            val formattedDate: String = targetFormat.format(date)
            peopleBirthdate.text = formattedDate
        } else {
            peopleBirthdate.visibility = View.GONE
        }

        peopleBirthplace.text = it.placeOfBirth
        peopleName.text = it.name

        if (!it.biography.isNullOrEmpty()) {
            peopleBiography.text = it.biography
        } else {
            bioLayout.visibility = View.GONE
        }
        if (!it.alsoKnownAs.isNullOrEmpty()) {
            textViewAdapter = TextViewAdapter(it.alsoKnownAs, this)
            linearLayoutManager = LinearLayoutManager(this)
            linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            alsoKnowAsView.layoutManager = linearLayoutManager
            alsoKnowAsView.setHasFixedSize(true)
            alsoKnowAsView.adapter = textViewAdapter
        } else {
            knownAsLayout.visibility = View.GONE
        }
        peopleDepartment.text = it.knownForDepartment
        var profilePath = it.profilePath
        if (!profilePath.isNullOrEmpty()) {
            val finalPath = IMAGE_BASE_URL + profilePath
            Glide.with(this).load(finalPath).into(peopleImage)
        } else {
            Glide.with(this).load(R.drawable.no_image_found).into(peopleImage)
        }

    }

    private fun getViewMode(peopleID: Int): PeopleDetailViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PeopleDetailViewModel(peopleDetailRepository, peopleID) as T
            }
        })[PeopleDetailViewModel::class.java]
    }
}
