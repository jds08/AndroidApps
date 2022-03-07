package com.example.flixster

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RatingBar
import android.widget.TextView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import okhttp3.Headers

private const val YOUTUBE_API_Key = "AIzaSyA-KOeddsFcTiC31AcN6xK_14JaAqTqvPE"
private const val TRAILERS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"

private const val TAG = "DetailActivity"
class DetailActivity : YouTubeBaseActivity() {
    private lateinit var tvTitle: TextView
    private lateinit var  tvOverview: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var ytPlayerView: YouTubePlayerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        tvTitle = findViewById(R.id.tvTitle)
        tvOverview = findViewById(R.id.tvOverview)
        ratingBar = findViewById(R.id.rbVoteAverage)
        ytPlayerView = findViewById(R.id.player)
        val movie = intent.getParcelableExtra<Movies>(MOVIE_EXTRA) as Movies
        Log.i(TAG, "Movies is $movie")
        tvTitle.text = movie.title
        tvOverview.text = movie.overview
        ratingBar.rating = movie.voteAverage.toFloat()

        val client = AsyncHttpClient()
        client.get(TRAILERS_URL.format(movie.movieId), object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.i(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess $statusCode")
                val results = json.jsonObject.getJSONArray("results")
                if (results.length() == 0) {
                    Log.w(TAG, "No movie trailers found")
                    return
                }
                val movieTrailerJSon = results.getJSONObject(0)
                val youtubeKey = movieTrailerJSon.getString("key")
                // play yotube video with this trailer
                initializeYoutube(youtubeKey)

            }
        })
    }

    private fun initializeYoutube(youtubeKey: String) {
        ytPlayerView.initialize(YOUTUBE_API_Key, object: YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(
                provide: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                Log.i(TAG, "onInitializationSuccess")
                player?.cueVideo(youtubeKey)
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Log.i(TAG, "onInitializationSuccess")
            }
        })

    }
}