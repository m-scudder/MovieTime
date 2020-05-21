package com.scudderapps.moviesup.fragments.moviedetails

import android.content.Intent
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
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.ms.square.android.expandabletextview.ExpandableTextView
import com.scudderapps.moviesup.CollectionActivity
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.api.IMAGE_BASE_URL
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.models.main.Genre
import com.scudderapps.moviesup.models.movie.CollectionResponse
import com.scudderapps.moviesup.models.movie.ProductionCompany
import com.scudderapps.moviesup.models.movie.ProductionCountry
import com.scudderapps.moviesup.models.movie.SpokenLanguage
import com.scudderapps.moviesup.repository.movie.moviedetails.MovieDetailRepository
import com.scudderapps.moviesup.viewmodel.MovieDetailViewModel
import java.text.NumberFormat
import java.util.*


class MovieAboutFragment(private var movieId: Int) : Fragment() {

    private lateinit var rootView: View

    @BindView(R.id.movie_overview)
    lateinit var movieOverview: ExpandableTextView

    @BindView(R.id.genresName)
    lateinit var genresName: TextView

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
    lateinit var collectionLayout: LinearLayout

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var movieRepository: MovieDetailRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.movie_about_fragment, container, false)
        ButterKnife.bind(this, rootView)
        val apiService: ApiInterface = TheTMDBClient.getClient()
        movieRepository = MovieDetailRepository(apiService)
        viewModel = getViewModel(movieId)
        populatingViews()
        return rootView
    }

    private fun populatingViews() {
        viewModel.movieDetails.observe(this, Observer {
            movieOverview.text = it.overview
            originalTitle.text = it.originalTitle
            movieRuntime.text = it.runtime.toString() + " Min"
            budget.text = NumberFormat.getCurrencyInstance(Locale("en", "US"))
                .format(it.budget.toDouble())
            revenue.text = NumberFormat.getCurrencyInstance(Locale("en", "US"))
                .format(it.revenue.toDouble())
            originalLanguage.text = it.originalLanguage
            val genre: ArrayList<Genre> = it.genres
            if (!genre.isNullOrEmpty()) {
                for (i in genre) {
                    genresName.append("\u25CF ${i.name}  ")
                }
            } else {
                genresLayout.visibility = View.GONE
            }

            val spokenLangaugeList : List<SpokenLanguage> = it.spokenLanguages
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
                collectionImage.setOnClickListener(View.OnClickListener {
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
