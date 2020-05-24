package com.scudderapps.moviesup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView

class TvGenreActivity : AppCompatActivity() {

    @BindView(R.id.tv_genre_view)
    lateinit var tvGenreView: RecyclerView

    @BindView(R.id.tv_genre_toolbar)
    lateinit var tvGenreToolbar: Toolbar

    @BindView(R.id.tv_genre_bar)
    lateinit var tvGenreBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_genre)
    }
}
