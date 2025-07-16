package com.icerrate.popularmovies.provider.cloud.api

import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.data.model.PaginatedResponse
import com.icerrate.popularmovies.data.model.Review
import com.icerrate.popularmovies.data.model.Trailer
import com.icerrate.popularmovies.data.model.TrailersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author Ivan Cerrate.
 */
interface MovieAPI {

    @GET(ServerPaths.POPULAR_MOVIES)
    fun getPopularMovies(@Query(ServerPaths.QUERY_PAGE) page: Int?): Call<PaginatedResponse<Movie>>

    @GET(ServerPaths.TOP_RATED_MOVIES)
    fun getTopRatedMovies(@Query(ServerPaths.QUERY_PAGE) page: Int?): Call<PaginatedResponse<Movie>>

    @GET(ServerPaths.SEARCH_MOVIES)
    fun searchMovies(
        @Query(ServerPaths.QUERY_STRING) query: String,
        @Query(ServerPaths.QUERY_PAGE) page: Int?
    ): Call<PaginatedResponse<Movie>>

    @GET(ServerPaths.TRAILERS_MOVIES)
    fun getMovieTrailers(@Path(ServerPaths.MOVIE_ID) movieId: Int?): Call<TrailersResponse<Trailer>>

    @GET(ServerPaths.REVIEWS)
    fun getMovieReviews(@Path(ServerPaths.MOVIE_ID) movieId: Int?): Call<PaginatedResponse<Review>>
}