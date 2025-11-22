package com.app.movies.list.repository

import android.util.Log
import com.app.movies.list.data.FavoriteDao
import com.app.movies.list.model.FavoriteEntity
import com.app.movies.list.model.Movie
import com.app.movies.list.network.MovieApi
import com.app.movies.list.ui.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val api: MovieApi,
    private val dao: FavoriteDao
) {

    fun getMovies(): Flow<UiState<List<Movie>>> = flow {
        emit(UiState.Loading)

        try {
            val movies = api.getFilms()
            emit(UiState.Success(movies))
            Log.d("apiCallingLogs","Data from api: $movies")
        } catch (e: Exception) {
            Log.e("apiCallingLogs","Error: $e")

            emit(UiState.Error(e.message ?: "Unknown error"))
        }
    }
    fun observeFavorites(): Flow<List<FavoriteEntity>> = dao.observeAllFavorites()

    suspend fun addFavorite(movieId: String) {
        dao.insert(FavoriteEntity(id = movieId))
    }

    suspend fun removeFavorite(movieId: String) {
        dao.deleteById(movieId)
    }

    fun isFavoriteFlow(id: String): Flow<Boolean> = dao.isFavoriteFlow(id)
}