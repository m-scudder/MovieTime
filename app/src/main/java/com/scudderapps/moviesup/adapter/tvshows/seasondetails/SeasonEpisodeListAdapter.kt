package com.scudderapps.moviesup.adapter.tvshows.seasondetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.models.tv.Episode
import kotlinx.android.synthetic.main.tv_season_episode_list_item.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class SeasonEpisodeListAdapter(
    private val episodes: List<Episode>,
    private val context: Context
) :
    RecyclerView.Adapter<SeasonEpisodeListAdapter.TvSeasonEpisodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvSeasonEpisodeHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.tv_season_episode_list_item, parent, false)
        return TvSeasonEpisodeHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: TvSeasonEpisodeHolder, position: Int) {
        val episodeItem = episodes[position]
        holder.bindVideos(episodeItem, context)
    }

    override fun getItemCount() = episodes.size

    class TvSeasonEpisodeHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var episode: Episode? = null
        private lateinit var context: Context

        fun bindVideos(episode: Episode, context: Context) {
            this.episode = episode
            this.context = context
            if (!episode!!.name.isNullOrEmpty()) {
                itemView.tvSeasonEpisodeName.text = episode!!.name
            }
            itemView.tvSeasonEpisodeNumber.text = episode!!.episodeNumber.toString()

            if (!episode.airDate.isNullOrEmpty()) {
                val originalFormat: DateFormat = SimpleDateFormat("yyyy-MM-d", Locale.ENGLISH)
                val targetFormat: DateFormat =
                    SimpleDateFormat(context.getString(R.string.dateFormat))
                val date: Date = originalFormat.parse(episode.airDate)
                val formattedDate: String = targetFormat.format(date)
                itemView.tvSeasonEpisodeAirDate.text = formattedDate
            }
//            itemView.setOnClickListener(View.OnClickListener {
//
//                val intent = Intent(context, SeasonActivity::class.java)
//                intent.putExtra("episodeNumber", episode?.episodeNumber)
//                intent.putExtra("tvId", tvId)
//                val options = ActivityOptions.makeSceneTransitionAnimation(
//                    context as Activity?,
//                    UtilPair<View, String>(itemView.tvSeasonPoster, "seasonPosterTransition")
//                )
//                context.startActivity(intent, options.toBundle())
//            })
        }
    }
}