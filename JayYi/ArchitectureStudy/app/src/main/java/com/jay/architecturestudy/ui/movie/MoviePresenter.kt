package com.jay.architecturestudy.ui.movie

import android.util.Log
import com.jay.architecturestudy.data.database.entity.MovieEntity
import com.jay.architecturestudy.data.model.Movie
import com.jay.architecturestudy.data.repository.NaverSearchRepositoryImpl
import com.jay.architecturestudy.ui.BaseSearchPresenter
import com.jay.architecturestudy.util.addTo
import com.jay.architecturestudy.util.singleIoMainThread
import com.jay.architecturestudy.util.then
import io.reactivex.Single

class MoviePresenter(
    override val view: MovieContract.View,
    override val repository: NaverSearchRepositoryImpl
) : BaseSearchPresenter(view, repository), MovieContract.Presenter {

    override fun subscribe() {
        val lastKeyword = repository.getLatestMovieKeyword()
        loadMovieSearchHistory(
            keyword = lastKeyword
        )
            .subscribe({
                view.updateUi(it.first, it.second)
            }, { e ->
                val message = e.message ?: return@subscribe
                Log.e("movie", message)
            })
            .addTo(disposables)
    }

    private fun loadMovieSearchHistory(keyword: String) : Single<Pair<String, List<Movie>>> {
        return if (keyword.isBlank()) {
            Single.just(Pair(keyword, emptyList()))
        } else {
            repository.getLatestMovieResult()
                .map { Pair(keyword, it) }
                .compose(singleIoMainThread())
        }
    }

    override fun search(keyword: String) {
        repository.getMovie(
            keyword = keyword
        )
            .map {
                // 기존 결과 삭제
                clearSearchHistory { repository.clearMovieResult() }
                it.movies.isNotEmpty().then {
                    val movieList = arrayListOf<MovieEntity>()
                    it.movies.mapTo(movieList) { movie ->
                        MovieEntity(
                            title = movie.title,
                            link =  movie.link,
                            image = movie.image,
                            subtitle = movie.subtitle,
                            director = movie.director,
                            actor = movie.actor,
                            pubDate = movie.pubDate,
                            userRating = movie.userRating
                        )
                    }
                    // 최신 결과 저장
                    updateSearchHistory { repository.saveMovieResult(movieList) }
                }
                repository.saveMovieKeyword(keyword)
                it.movies
            }
            .compose(singleIoMainThread())
            .subscribe({ movies ->
                if (movies.isEmpty()) {
                    view.hideResultListView()
                    view.showEmptyResultView()
                } else {
                    view.hideEmptyResultView()
                    view.showResultListView()
                }
                view.updateResult(movies)
            }, { e ->
                handleError(e)
            })
            .addTo(disposables)
    }
}