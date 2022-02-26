package com.example.flixster



import org.json.JSONArray

data class Movies (
    val movieId: Int,
    private val posterPath: String,
    val title: String,
    val overview: String,

    ){
    val posterImageUrl = "https://image.tmdb.org/t/p/w342/$posterPath"
    companion object{
        fun fromJsonArray(movieJsonArray: JSONArray): List<Movies>{
            val movies = mutableListOf<Movies>()
            for( i  in 0 until movieJsonArray.length()){
                val movieJson = movieJsonArray.getJSONObject(i)
                movies.add(
                    Movies(
                         movieJson.getInt("id"),
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