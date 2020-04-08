package com.scudderapps.moviesup

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife

class SearchActivity : AppCompatActivity() {

    @BindView(R.id.search_toolbar)
    lateinit var searchToolbar: Toolbar

    @BindView(R.id.search_edit_box)
    lateinit var searchEditTextView: EditText

    @BindView(R.id.search_recycler_view)
    lateinit var searchView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)
        ButterKnife.bind(this)
        setSupportActionBar(searchToolbar)
        supportActionBar!!.title = "Search"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }
}