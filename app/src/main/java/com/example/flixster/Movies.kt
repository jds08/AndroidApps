package com.example.flixster



import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import org.json.JSONArray
import org.parceler.Parcel

@Parcelize
data class Movies (
    val movieId: Int,
    val voteAverage: Double,
    private val posterPath: String,
    val title: String,
    val overview: String,

    ) : Parcelable {
    @IgnoredOnParcel
    val posterImageUrl = "https://image.tmdb.org/t/p/w342/$posterPath"
    companion object{
        fun fromJsonArray(movieJsonArray: JSONArray): List<Movies>{
            val movies = mutableListOf<Movies>()
            for( i  in 0 until movieJsonArray.length()){
                val movieJson = movieJsonArray.getJSONObject(i)
                movies.add(
                    Movies(
                         movieJson.getInt("id"),
                        movieJson.getDouble("vote_average"),
                         movieJson.getString("poster_path"),
                         movieJson.getString("title"),
                        movieJson.getString("overview")

                    )
                )
            }
            return movies

        }
    }
}