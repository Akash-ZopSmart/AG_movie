package com.example.ag_movie

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String = "a48d3dd4e2946703ac638b01dac9eca5",
        @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String = "a48d3dd4e2946703ac638b01dac9eca5",
        @Query("page") page: Int
    ): Call<GetMoviesResponse>
    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String = "a48d3dd4e2946703ac638b01dac9eca5",
        @Query("page") page: Int
    ): Call<GetMoviesResponse>
}
