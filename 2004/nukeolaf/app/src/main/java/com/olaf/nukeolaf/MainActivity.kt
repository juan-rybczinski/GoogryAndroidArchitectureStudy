package com.olaf.nukeolaf

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var retrofitClient: RetrofitInterface
    private val itemListener: MovieItemListener = object : MovieItemListener {
        override fun onMovieItemClick(item: MovieItem) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
            startActivity(intent)
        }
    }
    private val movieAdapter = MovieAdapter(ArrayList(0), itemListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retrofitClient = RetrofitClient.client
        movie_rv.adapter = movieAdapter

        search_view.setIconifiedByDefault(false)
        search_view.queryHint = "영화 검색"
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchMovie(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun searchMovie(query: String) {
        retrofitClient.searchMovie(
            getString(R.string.naver_client_id),
            getString(R.string.naver_client_secret),
            query, 10, 1
        ).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(
                call: Call<MovieResponse>,
                response: Response<MovieResponse>
            ) {
                if (response.isSuccessful) {
                    showMovies(response.body()!!.items).also {
                        Log.d("searchMovie", response.toString())
                        Log.d("searchMovie", response.body().toString())
                    }
                } else {

                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.d("searchMovie", t.toString())
            }
        })
    }

    private fun showMovies(movies: ArrayList<MovieItem>) {
        movieAdapter.run {
            setMovies(movies)
            notifyDataSetChanged()
        }
    }

    private fun makeToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }
}
