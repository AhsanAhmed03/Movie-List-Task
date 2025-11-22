package com.app.movies.list.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Movie(
    val id: String,
    val title: String,
    val original_title: String? = null,
    val image: String? = null,
    val description: String? = null,
    val release_date: String? = null
) : Parcelable