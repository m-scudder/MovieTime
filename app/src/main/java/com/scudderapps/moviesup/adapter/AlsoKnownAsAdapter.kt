package com.scudderapps.moviesup.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scudderapps.moviesup.R
import kotlinx.android.synthetic.main.cast_known_as_item.view.*

class AlsoKnownAsAdapter(private val details: List<String>, private val context: Context) :
    RecyclerView.Adapter<AlsoKnownAsAdapter.TextHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.cast_known_as_item, parent, false)
        return TextHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: TextHolder, position: Int) {
        val detailItem = details[position]
        holder.bindVideos(detailItem, context)
    }

    override fun getItemCount() = details.size

    class TextHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v
        private lateinit var details: String
        private lateinit var context: Context

        fun bindVideos(detail: String, context: Context) {
            this.details = detail
            this.context = context
            view.also_known_as.text = details
        }
    }
}