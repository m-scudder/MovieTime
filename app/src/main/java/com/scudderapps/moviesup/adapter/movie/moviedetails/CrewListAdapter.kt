package com.scudderapps.moviesup.adapter.movie.moviedetails

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scudderapps.moviesup.PeopleDetailActivity
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.api.IMAGE_BASE_URL
import com.scudderapps.moviesup.models.common.CrewDetail
import kotlinx.android.synthetic.main.crew_list_item.view.*
import android.util.Pair as UtilPair

class CrewListAdapter(private val crew: ArrayList<CrewDetail>, private val context: Context) :
    RecyclerView.Adapter<CrewListAdapter.CrewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.crew_list_item, parent, false)
        return CrewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: CrewHolder, position: Int) {
        val crewItem = crew[position]
        holder.bindVideos(crewItem, context)
    }

    override fun getItemCount() = crew.size

    class CrewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v
        private var crew: CrewDetail? = null
        private lateinit var context: Context

        fun bindVideos(crew: CrewDetail, context: Context) {
            this.crew = crew
            this.context = context
            itemView.crewName.text = crew.name
            itemView.crewJob.text = crew.job
            if (!crew.profilePath.isNullOrEmpty()) {
                val profileUrl = IMAGE_BASE_URL + crew?.profilePath
                Glide.with(view)
                    .load(profileUrl)
                    .into(itemView.crewImage)
            } else {
                Glide.with(view)
                    .load(R.drawable.default_avatar)
                    .into(itemView.crewImage)
            }

            itemView.setOnClickListener(View.OnClickListener {

                val intent = Intent(context, PeopleDetailActivity::class.java)
                intent.putExtra("id", crew?.id)
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    context as Activity?,
                    UtilPair<View, String>(itemView.crewImage, "peopleImageTransition")
                )
                context.startActivity(intent, options.toBundle())
            })
        }
    }
}