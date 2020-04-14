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

    @BindView(R.id.images_layout)
    lateinit var imagesLayout: LinearLayout

    @BindView(R.id.people_media)
    lateinit var peopleMedia: ImageView

    @BindView(R.id.people_count)
    lateinit var peopleCount: TextView

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
            movieAdapter = MovieAdapter(it.cast, this)
            movieCreditList.layoutManager = linearLayoutManager2
            movieCreditList.setHasFixedSize(true)
            movieCreditList.adapter = movieAdapter
        })
        peopleDetailViewModel.peopleImages.observe(this, Observer {
            peopleProfiles = it.profiles
            if (!peopleProfiles.isNullOrEmpty()) {
                imagesLayout.visibility = View.VISIBLE
                var mediaPosterURL = peopleProfiles[0].filePath
                val mediaPosterUrl: String = IMAGE_BASE_URL + mediaPosterURL
                Glide.with(this).load(mediaPosterUrl).into(peopleMedia)
                peopleCount.text = peopleProfiles.size.toString() + " Images"
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
                imagesLayout.visibility = View.GONE
            }
        })

    }

    fun bindUI(it: PeopleDetails) {

        if (!it.birthday.isNullOrEmpty()) {
            val originalFormat: DateFormat = SimpleDateFormat("yyyy-MM-d", Locale.ENGLISH)
            val targetFormat: DateFormat = SimpleDateFormat(getString(R.string.dateFormat))
            val date: Date = originalFormat.parse(it.birthday)
            val formattedDate: String = targetFormat.format(date)
            peopleBirthdate.text = "Born on : $formattedDate"
        } else {
            peopleBirthdate.visibility = View.GONE
        }

        if (!it.placeOfBirth.isNullOrEmpty()) {
            peopleBirthplace.text = "From : " + it.placeOfBirth
        } else {
            peopleBirthplace.visibility = View.GONE
        }
        peopleName.text = it.name
        peopleBiography.text = it.biography
        var profilePath = it.profilePath
        val finalPath = IMAGE_BASE_URL + profilePath
        peopleDepartment.text = "Department: " + it.knownForDepartment
        Glide.with(this).load(finalPath).into(peopleImage)

        textViewAdapter = TextViewAdapter(it.alsoKnownAs, this)
        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        alsoKnowAsView.layoutManager = linearLayoutManager
        alsoKnowAsView.setHasFixedSize(true)
        alsoKnowAsView.adapter = textViewAdapter

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
