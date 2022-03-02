package com.example.ag_movie

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.ag_movie.data.MovieDB
import com.example.ag_movie.data.MovieEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val MOVIE_BACKDROP = "extra_movie_backdrop"
const val MOVIE_POSTER = "extra_movie_poster"
const val MOVIE_TITLE = "extra_movie_title"
const val MOVIE_RATING = "extra_movie_rating"
const val MOVIE_RELEASE_DATE = "extra_movie_release_date"
const val MOVIE_OVERVIEW = "extra_movie_overview"

class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var backdrop: ImageView
    private lateinit var poster: ImageView
    private lateinit var title: TextView
    private lateinit var rating: RatingBar
    private lateinit var releaseDate: TextView
    private lateinit var overview: TextView
    private lateinit var favouriteBtn: ImageButton
    private lateinit var database: MovieDB

    private var movieIsLiked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        backdrop = findViewById(R.id.movie_backdrop)
        poster = findViewById(R.id.movie_poster)
        title = findViewById(R.id.movie_title)
        rating = findViewById(R.id.movie_rating)
        releaseDate = findViewById(R.id.movie_release_date)
        overview = findViewById(R.id.movie_overview)
        favouriteBtn = findViewById(R.id.button)

        val extras = intent.extras
        setDBInstance()
        checkLikeButtonState()

        favouriteBtn.setOnClickListener {
            Log.d("Button", "onCreate: Button Pressed!")
            movieIsLiked = !movieIsLiked
            if (movieIsLiked) {
                GlobalScope.launch {
                    database.movieDao().insertMovie(
                        MovieEntity(
                            0,
                            intent.getStringExtra("movieTitle").toString(),
                            movieIsLiked
                        )
                    )
                }
            } else {
                GlobalScope.launch {
                    database.movieDao()
                        .deleteByMovieTitle(intent.getStringExtra("movieTitle").toString())
                }
            }
            updateFavouriteIconColor(movieIsLiked)
        }

        if (extras != null) {
            populateDetails(extras)
        } else {
            finish()
        }
    }

    private fun updateFavouriteIconColor(movieIsLiked: Boolean) {
        when (movieIsLiked) {
            true -> favouriteBtn.setImageDrawable(
                AppCompatResources.getDrawable(
                    applicationContext,
                    R.drawable.ic_favourite_red
                )
            )
            false -> favouriteBtn.setImageDrawable(
                AppCompatResources.getDrawable(
                    applicationContext,
                    R.drawable.ic_favourite_white
                )
            )
        }
    }

    private fun populateDetails(extras: Bundle) {
        extras.getString(MOVIE_BACKDROP)?.let { backdropPath ->
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w1280$backdropPath")
                .transform(CenterCrop())
                .into(backdrop)
        }

        extras.getString(MOVIE_POSTER)?.let { posterPath ->
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w342$posterPath")
                .transform(CenterCrop())
                .into(poster)
        }

        title.text = extras.getString(MOVIE_TITLE, "")
        rating.rating = extras.getFloat(MOVIE_RATING, 0f) / 2
        releaseDate.text = extras.getString(MOVIE_RELEASE_DATE, "")
        overview.text = extras.getString(MOVIE_OVERVIEW, "")
    }

    private fun setDBInstance() {
        database = Room.databaseBuilder(
            applicationContext,
            MovieDB::class.java,
            "movieDB"
        ).build()
    }

    private fun checkLikeButtonState() {
        database.movieDao().getMovies().observe(
            this,
            Observer {
                for (i in it.indices) {
                    if (it.get(i).movieTitle.equals(
                            intent.getStringExtra("movieTitle").toString()
                        )
                    ) {
                        updateFavouriteIconColor(true)
                    }
                }
            }
        )
    }
}
