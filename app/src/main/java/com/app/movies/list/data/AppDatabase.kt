package com.app.movies.list.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.movies.list.model.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}