package com.app.movies.list.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.movies.list.model.Movie
import com.app.movies.list.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _moviesState = MutableStateFlow<UiState<List<Movie>>>(UiState.Loading)
    val moviesState: StateFlow<UiState<List<Movie>>> = _moviesState.asStateFlow()

    // Observes favorite movie IDs stored in Room
    private val favoriteIdsFlow = repository.observeFavorites()
        .map { entities -> entities.map { it.id }.toSet() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptySet())

    // Holds the latest fetched movie list
    private val allMoviesFlow = MutableStateFlow<List<Movie>>(emptyList())

    // Holds the selected filter state
    private val _filterState = MutableStateFlow(FilterType.ALL)
    val filterState: StateFlow<FilterType> = _filterState.asStateFlow()

    // Fetch movies from the repository and update UI state
    fun loadMovies() {
        viewModelScope.launch {
            repository.getMovies().collect { state ->
                _moviesState.value = state
                if (state is UiState.Success) {
                    allMoviesFlow.value = state.data
                }
            }
        }
    }

    // Exposes filtered movie list based on user selection
    val filteredMovies: StateFlow<List<Movie>> = combine(
        allMoviesFlow, favoriteIdsFlow, _filterState
    ) { movies, favoriteIds, filter ->
        when (filter) {
            FilterType.ALL -> movies
            FilterType.FAVORITES -> movies.filter { favoriteIds.contains(it.id) }
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Update filter selection
    fun setFilter(type: FilterType) {
        _filterState.value = type
    }

    // Handles favorite toggle for a movie
    fun toggleFavorite(movieId: String, isCurrentlyFavorite: Boolean) {
        viewModelScope.launch {
            if (isCurrentlyFavorite) {
                repository.removeFavorite(movieId)
            } else {
                repository.addFavorite(movieId)
            }
        }
    }

    // Expose favorite IDs to UI
    fun favoriteIds(): StateFlow<Set<String>> = favoriteIdsFlow

}
