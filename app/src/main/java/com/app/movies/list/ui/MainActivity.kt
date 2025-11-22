package com.app.movies.list.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.movies.list.MoviesAdapter
import com.app.movies.list.R
import com.app.movies.list.databinding.ActivityMainBinding
import com.app.movies.list.model.Movie
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MoviesViewModel by viewModels()
    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeUiState()
        observeMovieList()
        observeFavoriteIds()

        viewModel.loadMovies()

        binding.btnRetry.setOnClickListener { viewModel.loadMovies() }

        binding.radioFilter.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioAll -> viewModel.setFilter(FilterType.ALL)
                R.id.radioFavorites -> viewModel.setFilter(FilterType.FAVORITES)
            }
        }
    }

    private fun setupRecyclerView() {
        moviesAdapter = MoviesAdapter(
            onItemClicked = { movie -> navigateToDetails(movie) },
            onFavClicked = { movieId, isFavorite ->
                viewModel.toggleFavorite(movieId, isFavorite)
            },
        )

        binding.rvMovies.apply {
            adapter = moviesAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun observeUiState() {
        with(binding) {
            lifecycleScope.launch {
                viewModel.moviesState.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                            progressBar.visibility = View.VISIBLE
                            tvError.visibility = View.GONE
                            btnRetry.visibility = View.GONE
                            rvMovies.visibility = View.GONE
                        }

                        is UiState.Error -> {
                            progressBar.visibility = View.GONE
                            tvError.visibility = View.VISIBLE
                            tvError.text = state.message ?: getString(R.string.error)
                            btnRetry.visibility = View.VISIBLE
                            rvMovies.visibility = View.GONE
                        }

                        is UiState.Success -> {
                            progressBar.visibility = View.GONE
                            tvError.visibility = View.GONE
                            btnRetry.visibility = View.GONE
                            rvMovies.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun observeMovieList() {
        lifecycleScope.launch {
            viewModel.filteredMovies.collect { movies ->

                moviesAdapter.submitList(movies)

                if (movies.isEmpty()) {
                    binding.tvNoData.visibility = View.VISIBLE
                } else {
                    binding.tvNoData.visibility = View.GONE
                    binding.rvMovies.visibility = View.VISIBLE
                }
            }
        }
    }


    private fun observeFavoriteIds() {
        lifecycleScope.launch {
            viewModel.favoriteIds().collect { favorites ->
                moviesAdapter.submitFavorites(favorites)
            }
        }
    }

    private fun navigateToDetails(movie: Movie) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("movie", movie)
        startActivity(intent)
    }

}
