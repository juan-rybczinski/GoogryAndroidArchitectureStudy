package com.example.googryandroidarchitecturestudy.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.googryandroidarchitecturestudy.R
import com.example.googryandroidarchitecturestudy.databinding.FragmentMovieSearchBinding
import com.example.googryandroidarchitecturestudy.ui.extension.toast
import com.example.googryandroidarchitecturestudy.ui.recycler.MovieAdapter
import com.example.googryandroidarchitecturestudy.ui.viewmodel.MovieSearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieSearchFragment :
    BaseFragment<FragmentMovieSearchBinding, MovieSearchViewModel>(R.layout.fragment_movie_search) {
    private val args: MovieSearchFragmentArgs by navArgs()

    private val movieAdapter = MovieAdapter {
        viewModel.selectUrlItem(it)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
    }

    private fun setupUi() {
        with(binding) {
            movieList.adapter = movieAdapter
        }

        checkPassedQuery()

        with(viewModel) {
            showQueryEmptyEvent.observe(viewLifecycleOwner) {
                showQueryEmpty()
            }

            showNoSearchResultEvent.observe(viewLifecycleOwner) {
                showNoSearchResult()
            }

            showSearchMovieFailedEvent.observe(viewLifecycleOwner) {
                showSearchFailed(it)
            }

            showSaveRecentFailedEvent.observe(viewLifecycleOwner) {
                showSaveRecentFailed(it)
            }

            showRecentKeywordsEvent.observe(viewLifecycleOwner) {
                navToRecentSearch()
            }
        }
    }

    private fun checkPassedQuery() {
        args.passedQuery?.let {
            viewModel.query.value = it
            viewModel.queryMovieList()
        }
    }

    private fun showQueryEmpty() {
        toast(getString(R.string.no_keyword))
        movieAdapter.setMovies(listOf())
    }

    private fun showNoSearchResult() {
        toast(getString(R.string.no_results))
        movieAdapter.setMovies(listOf())
    }

    private fun showSearchFailed(e: Exception) {
        toast(getString(R.string.error_occurred))
        Log.e(TAG, "Searching movies from keyword failed: ${e.message.toString()}")
    }

    private fun showSaveRecentFailed(e: Exception) {
        Log.e(TAG, "Saving recent keyword failed: ${e.message.toString()}")
    }

    private fun navToRecentSearch() {
        val action = MovieSearchFragmentDirections.actionMovieSearchFragmentToMovieRecentFragment()
        this.findNavController().navigate(action)
    }

    companion object {
        private val TAG = MovieSearchFragment::class.java.simpleName
    }
}