package com.scudderapps.moviesup.fragments.moviedetails

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.scudderapps.moviesup.CollectionActivity
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.adapter.common.TrailerListAdapter
import com.scudderapps.moviesup.adapter.movie.MovieGenreListAdapter
import com.scudderapps.moviesup.api.IMAGE_BASE_URL
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.api.TmdbApiInterface
import com.scudderapps.moviesup.models.common.Backdrop
import com.scudderapps.moviesup.models.common.Poster
import com.scudderapps.moviesup.models.common.ProductionCompany
import com.scudderapps.moviesup.models.movie.CollectionResponse
import com.scudderapps.moviesup.models.movie.ProductionCountry
import com.scudderapps.moviesup.models.movie.SpokenLanguage
import com.scudderapps.moviesup.repository.movie.moviedetails.MovieDetailRepository
import com.scudderapps.moviesup.viewmodel.MovieDetailViewModel
import com.stfalcon.imageviewer.StfalconImageViewer
import java.text.NumberFormat
import java.util.*


class MovieAboutFragment(private var movieId: Int) : Fragment() {

    private lateinit var rootView: View

    @BindView(R.id.movie_overview)
    lateinit var movieOverview: ExpandableTextView

    @BindView(R.id.movie_genre_list)
    lateinit var movieGenreListView: RecyclerView

    @BindView(R.id.original_title)
    lateinit var originalTitle: TextView

    @BindView(R.id.budget)
    lateinit var budget: TextView

    @BindView(R.id.revenue)
    lateinit var revenue: TextView

    @BindView(R.id.original_language)
    lateinit var originalLanguage: TextView

    @BindView(R.id.movie_runtime)
    lateinit var movieRuntime: TextView

    @BindView(R.id.spoken_language)
    lateinit var spokenLanguage: TextView

    @BindView(R.id.production_companies)
    lateinit var productionCompany: TextView

    @BindView(R.id.production_countries)
    lateinit var productionCountry: TextView

    @BindView(R.id.genres_layout)
    lateinit var genresLayout: LinearLayout

    @BindView(R.id.collection_media)
    lateinit var collectionImage: ImageView

    @BindView(R.id.collection_name)
    lateinit var collectionName: TextView

    @BindView(R.id.collection_layout)
    lateinit var collectionLayout: ConstraintLayout

    @BindView(R.id.trailerListView)
    lateinit var trailerListView: RecyclerView

    @BindView(R.id.trailer_layout)
    lateinit var trailerLayout: LinearLayout

    @BindView(R.id.posterMedia)
    lateinit var posterMedia: ImageView

    @BindView(R.id.backdropMedia)
    lateinit var backdropMedia: ImageView

    @BindView(R.id.posterCount)
    lateinit var poster_count: TextView

    @BindView(R.id.backdropCount)
    lateinit var backdrop_count: TextView

    @BindView(R.id.posterlayout)
    lateinit var posterLayout: LinearLayout

    @BindView(R.id.backdropLayout)
    lateinit var backdropLayout: LinearLayout

    @BindView(R.id.view_collection)
    lateinit var viewCollection: Button

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var movieRepository: MovieDetailRepository

    private lateinit var movieGenreListAdapter: MovieGenreListAdapter
    private lateinit var trailerAdapter: TrailerListAdapter
    private lateinit var movieBackdrops: List<Backdrop>
    private lateinit var moviePosters: List<Poster>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.movie_about_fragment, container, false)
        ButterKnife.bind(this, rootView)
        val apiService: TmdbApiInterface = TheTMDBClient.getClient()
        movieRepository = MovieDetailRepository(apiService)
        viewModel = getViewModel(movieId)
        populatingViews()
        return rootView
    }

    private fun populatingViews() {
        viewModel.movieDetails.observe(viewLifecycleOwner, Observer {
            movieOverview.text = it.overview
            originalTitle.text = it.originalTitle

            if (it.runtime.toString() != "0")
                movieRuntime.text = it.runtime.toString() + " Min"
            else movieRuntime.text = "-"

            if (it.budget != "0")
                budget.text = NumberFormat.getCurrencyInstance(Locale("en", "US"))
                    .format(it.budget.toDouble())
            else budget.text = "-"

            if (it.revenue != "0")
                revenue.text = NumberFormat.getCurrencyInstance(Locale("en", "US"))
                    .format(it.revenue.toDouble())
            else revenue.text = "-"

            originalLanguage.text = "-"

            if (!it.genres.isNullOrEmpty()) {
                movieGenreListAdapter =
                    MovieGenreListAdapter(
                        it.genres,
                        rootView.context
                    )
                val linearLayoutManager2 = LinearLayoutManager(activity)
                linearLayoutManager2.orientation = LinearLayoutManager.HORIZONTAL
                movieGenreListView.layoutManager = linearLayoutManager2
                movieGenreListView.setHasFixedSize(true)
                movieGenreListView.adapter = movieGenreListAdapter
            } else {
                genresLayout.visibility = View.GONE
            }

            val spokenLangaugeList: List<SpokenLanguage> = it.spokenLanguages
            if (!spokenLangaugeList.isNullOrEmpty())
                for (i in spokenLangaugeList) {
//                    spokenLanguage.append("\u25CF ${i.name}  ")
                    spokenLanguage.text = spokenLangaugeList[0].name
                }
            else
                spokenLanguage.text = "-"

            val collectionList: CollectionResponse = it.belongsToCollection
            if (collectionList != null) {

                val collectionBackdrop: String = IMAGE_BASE_URL + collectionList.backdropPath
                Glide.with(this).load(collectionBackdrop).into(collectionImage)
                collectionName.text = "Belongs to " + collectionList.name
                viewCollection.setOnClickListener(View.OnClickListener {
                    val intent = Intent(activity, CollectionActivity::class.java)
                    intent.putExtra("id", collectionList.id)
                    startActivity(intent)
                })

            } else {
                collectionLayout.visibility = View.GONE
            }

            val productionCountryList: List<ProductionCountry> = it.productionCountries
            if (!productionCountryList.isNullOrEmpty()) productionCountry.text =
                productionCountryList[0].name else
                productionCountry.text = "-"

            val productionCompanyList: List<ProductionCompany> = it.productionCompanies
            if (!productionCompanyList.isNullOrEmpty()) productionCompany.text =
                productionCompanyList[0].name else
                productionCompany.text = "-"
        })

        viewModel.videoDetails.observe(viewLifecycleOwner, Observer {

            if (!it.videosList.isNullOrEmpty()) {
                trailerAdapter =
                    TrailerListAdapter(
                        it.videosList,
                        rootView.context
                    )
                val linearLayoutManager = LinearLayoutManager(activity)
                linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                trailerListView.layoutManager = linearLayoutManager
                trailerListView.setHasFixedSize(true)
                trailerListView.adapter = trailerAdapter
            } else {
                trailerLayout.visibility = View.GONE
            }

        })

        viewModel.movieMedia.observe(viewLifecycleOwner, Observer {
            movieBackdrops = it.backdrops
            moviePosters = it.posters
            if (!moviePosters.isNullOrEmpty()) {
                var mediaPosterURL = moviePosters[0].filePath
                val mediaPosterUrl: String = IMAGE_BASE_URL + mediaPosterURL
                Glide.with(this).load(mediaPosterUrl).into(posterMedia)
                if (moviePosters.size == 1) {
                    poster_count.text = moviePosters.size.toString() + " Poster"
                } else {
                    poster_count.text = moviePosters.size.toString() + " Posters"
                }
                posterMedia.setOnClickListener(View.OnClickListener {
                    StfalconImageViewer.Builder(
                        activity,
                        moviePosters
                    ) { posterMedia: ImageView, poster: Poster ->
                        Glide.with(this).load(IMAGE_BASE_URL + poster.filePath).into(posterMedia)
                    }
                        .withHiddenStatusBar(false)
                        .show()
                })
            } else {
                Glide.with(this).load(R.drawable.no_image_found).centerInside().into(posterMedia)
                poster_count.text = "No Posters"
            }
            if (!movieBackdrops.isNullOrEmpty()) {
                var mediaBackdropURL = movieBackdrops[0].filePath
                val mediaBackdropUrl: String = IMAGE_BASE_URL + mediaBackdropURL
                Glide.with(this).load(mediaBackdropUrl).into(backdropMedia)
                if (movieBackdrops.size == 1) {
                    backdrop_count.text = movieBackdrops.size.toString() + " Backdrop"
                } else {
                    backdrop_count.text = movieBackdrops.size.toString() + " Backdrops"
                }

                backdropMedia.setOnClickListener(View.OnClickListener {
                    StfalconImageViewer.Builder(
                        activity,
                        movieBackdrops
                    ) { backdropMedia: ImageView, backdrop: Backdrop ->
                        Glide.with(this).load(IMAGE_BASE_URL + backdrop.filePath)
                            .into(backdropMedia)
                    }
                        .withHiddenStatusBar(false)
                        .show()
                })
            } else {
                Glide.with(this).load(R.drawable.no_image_found).centerInside().into(backdropMedia)
                backdrop_count.text = "No Backdrops"
            }
        })
    }

    private fun getViewModel(movieId: Int): MovieDetailViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieDetailViewModel(movieRepository, movieId) as T
            }
        })[MovieDetailViewModel::class.java]
    }
}
