package com.app.movies.list.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: String
)