package com.icerrate.popularmovies.data.source.remote

import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.data.model.PaginatedResponse
import com.icerrate.popularmovies.data.model.Review
import com.icerrate.popularmovies.data.model.Trailer
import com.icerrate.popularmovies.data.model.TrailersResponse
import com.icerrate.popularmovies.data.source.MovieDataSource
import com.icerrate.popularmovies.provider.cloud.BaseService
import com.icerrate.popularmovies.provider.cloud.ServerServiceRequest
import com.icerrate.popularmovies.provider.cloud.api.MovieAPI
import com.icerrate.popularmovies.view.common.BaseCallback
import retrofit2.Call

/**
 * @author Ivan Cerrate.
 */
class MovieRemoteDataSource(private val movieAPI: MovieAPI) : BaseService(), MovieDataSource {

    override fun getPopularMovies(page: Int?, callback: BaseCallback<PaginatedResponse<Movie>>) {
        val call: Call<PaginatedResponse<Movie>> = movieAPI.getPopularMovies(page)
        val serviceRequest = ServerServiceRequest(call)
        enqueue(serviceRequest, callback)
    }

    override fun getTopRatedMovies(page: Int?, callback: BaseCallback<PaginatedResponse<Movie>>) {
        val call: Call<PaginatedResponse<Movie>> = movieAPI.getTopRatedMovies(page)
        val serviceRequest = ServerServiceRequest(call)
        enqueue(serviceRequest, callback)
    }

    override fun searchMovies(query: String, page: Int?, callback: BaseCallback<PaginatedResponse<Movie>>) {
        val call: Call<PaginatedResponse<Movie>> = movieAPI.searchMovies(query, page)
        val serviceRequest = ServerServiceRequest(call)
        enqueue(serviceRequest, callback)
    }

    override fun getMovieTrailers(movieId: Int?, callback: BaseCallback<TrailersResponse<Trailer>>) {
        val call: Call<TrailersResponse<Trailer>> = movieAPI.getMovieTrailers(movieId)
        val serviceRequest = ServerServiceRequest(call)
        enqueue(serviceRequest, callback)
    }

    override fun getMovieReviews(movieId: Int?, callback: BaseCallback<PaginatedResponse<Review>>) {
        val call: Call<PaginatedResponse<Review>> = movieAPI.getMovieReviews(movieId)
        val serviceRequest = ServerServiceRequest(call)
        enqueue(serviceRequest, callback)
    }

    override fun getFavoriteMovies(callback: BaseCallback<ArrayList<Movie>>) {
        throw UnsupportedOperationException("Operation is not available!")
    }

    override fun isFavoriteMovie(movieId: Int, callback: BaseCallback<Boolean>) {
        throw UnsupportedOperationException("Operation is not available!")
    }

    override fun addFavoriteMovie(movie: Movie, callback: BaseCallback<Unit>) {
        throw UnsupportedOperationException("Operation is not available!")
    }

    override fun removeFavoriteMovie(movieId: Int, callback: BaseCallback<Unit>) {
        throw UnsupportedOperationException("Operation is not available!")
    }
}