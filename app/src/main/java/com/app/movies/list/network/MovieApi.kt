package com.app.movies.list.network

import com.app.movies.list.model.Movie
import retrofit2.http.GET

interface MovieApi {
    @GET("films")
    suspend fun getFilms(): List<Movie>
}