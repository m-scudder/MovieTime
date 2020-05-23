package com.scudderapps.moviesup.adapter.tvshows

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.SeasonActivity
import com.scudderapps.moviesup.api.IMAGE_BASE_URL
import com.scudderapps.moviesup.models.tv.Season
import kotlinx.android.synthetic.main.tv_season_list_item.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import android.util.Pair as UtilPair

class TvSeasonListAdapter(
    val tvId: Int,
    private val seasons: List<Season>,
    private val context: Context
) :
    RecyclerView.Adapter<TvSeasonListAdapter.TvSeasonHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvSeasonHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.tv_season_list_item, parent, false)
        return TvSeasonHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: TvSeasonHolder, position: Int) {
        val castItem = seasons[position]
        holder.bindVideos(castItem, context, tvId)
    }

    override fun getItemCount() = seasons.size

    class TvSeasonHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v
        private var season: Season? = null
        private lateinit var context: Context

        fun bindVideos(season: Season, context: Context, tvId: Int) {
            this.season = season
            this.context = context
            itemView.tvSeasonName.text = season.name
            itemView.tvEpisodeCount.text = "Total Episodes " + season.episodeCount.toString()
            if (!season.airDate.isNullOrEmpty()) {
                val originalFormat: DateFormat = SimpleDateFormat("yyyy-MM-d", Locale.ENGLISH)
                val targetFormat: DateFormat =
                    SimpleDateFormat(context.getString(R.string.dateFormat))
                val date: Date = originalFormat.parse(season.airDate)
                val formattedDate: String = targetFormat.format(date)
                itemView.tvSeasonAirDate.text = formattedDate
            }

            if (!season.posterPath.isNullOrEmpty()) {
                val profileUrl = IMAGE_BASE_URL + season?.posterPath
                Glide.with(view)
                    .load(profileUrl)
                    .into(itemView.tvSeasonPoster)
            } else {
                Glide.with(view)
                    .load(R.drawable.no_image_found)
                    .into(itemView.tvSeasonPoster)
            }

            itemView.setOnClickListener(View.OnClickListener {

                val intent = Intent(context, SeasonActivity::class.java)
                intent.putExtra("seasonNumber", season?.seasonNumber)
                intent.putExtra("tvId", tvId)
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    context as Activity?,
                    UtilPair<View, String>(itemView.tvSeasonPoster, "seasonPosterTransition")
                )
                context.startActivity(intent, options.toBundle())
            })
        }
    }
}