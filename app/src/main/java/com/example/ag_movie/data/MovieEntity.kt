package com.example.ag_movie.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val movieTitle: String,
    val isMovieLiked: Boolean
)
