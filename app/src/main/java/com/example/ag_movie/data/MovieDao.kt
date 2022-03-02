package com.example.ag_movie.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDao {

    @Insert
    suspend fun insertMovie(movie: MovieEntity)

//    @Update
//    suspend fun updateMovie(movie:Movie)
//
//    @Delete
//    suspend fun deleteMovie(movie:Movie)
//
//    @Query("DELETE FROM movie")
//    suspend fun nukeTable()

    @Query("DELETE FROM movie WHERE movieTitle = :movieTitle")
    suspend fun deleteByMovieTitle(movieTitle: String)

    @Query("Select * from movie")
    fun getMovies(): LiveData<List<MovieEntity>>
}
