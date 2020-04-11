package com.scudderapps.moviesup

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent
import com.scudderapps.moviesup.adapter.SearchListAdapter
import com.scudderapps.moviesup.api.TheTMDBApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.models.Movie
import com.scudderapps.moviesup.models.MovieResponse
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.SingleSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class SearchActivity : AppCompatActivity() {

    @BindView(R.id.search_toolbar)
    lateinit var searchToolbar: Toolbar

    @BindView(R.id.search_edit_box)
    lateinit var searchEditTextView: EditText

    @BindView(R.id.search_recycler_view)
    lateinit var searchView: RecyclerView

    private val linearLayoutManager = LinearLayoutManager(this)
    private lateinit var searchAdapter: SearchListAdapter
    private val disposable = CompositeDisposable()
    private val publishSubject = PublishSubject.create<String>()
    lateinit var unbinder: Unbinder
    var searchMovieList: ArrayList<Movie> = ArrayList<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)
        unbinder = ButterKnife.bind(this)
        setSupportActionBar(searchToolbar)
        supportActionBar!!.title = "Search"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        linearLayoutManager.reverseLayout = false

        val apiService: TheTMDBApiInterface = TheTMDBClient.getClient()

        searchAdapter = SearchListAdapter(searchMovieList, this@SearchActivity)
        searchView.layoutManager = linearLayoutManager
        searchView.setHasFixedSize(true)
        searchView.adapter = searchAdapter

        val observer: DisposableObserver<MovieResponse> = getSearchObserver()

        disposable.add(
            publishSubject.debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMapSingle(object : Single<MovieResponse>(),
                    Function<String, SingleSource<MovieResponse>> {
                    @Throws(Exception::class)
                    override fun apply(s: String): Single<MovieResponse> {
                        return apiService.getSearchResults(s)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                    }

                    override fun subscribeActual(observer: SingleObserver<in MovieResponse>) {
                        TODO("Not yet implemented")
                    }
                })
                .subscribeWith(observer)
        )

        RxTextView.textChangeEvents(searchEditTextView)
            .skipInitialValue()
            .debounce(300, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(searchContactsTextWatcher())?.let {
                disposable.add(
                    it
                )
            }
        disposable.add(observer)
        publishSubject.onNext(" ")
    }

    private fun getSearchObserver(): DisposableObserver<MovieResponse> {
        return object : DisposableObserver<MovieResponse>() {
            override fun onNext(movies: MovieResponse) {
                searchMovieList.clear()
                searchMovieList.addAll(movies.movieList)
                Log.d("List", movies.movieList.toString())
                Log.d("Size", movies.totalResults.toString())
                searchAdapter.notifyDataSetChanged()

            }

            override fun onError(e: Throwable) {
                Log.e("FragmentActivity", "onError: " + e.message)
            }

            override fun onComplete() {}
        }
    }

    private fun searchContactsTextWatcher(): DisposableObserver<TextViewTextChangeEvent?>? {
        return object : DisposableObserver<TextViewTextChangeEvent?>() {
            override fun onNext(textViewTextChangeEvent: TextViewTextChangeEvent) {
                Log.d("SearchActivity", "Search query: " + textViewTextChangeEvent.text())
                publishSubject.onNext(textViewTextChangeEvent.text().toString())
            }

            override fun onError(e: Throwable) {
                Log.e("SearchActivity", "onError: " + e.message)
            }

            override fun onComplete() {}
        }
    }

    override fun onDestroy() {
        disposable.clear()
        unbinder.unbind()
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
    }
}