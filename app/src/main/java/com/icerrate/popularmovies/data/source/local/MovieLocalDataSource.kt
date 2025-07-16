package com.icerrate.popularmovies.data.source.local

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.icerrate.popularmovies.BuildConfig
import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.data.model.PaginatedResponse
import com.icerrate.popularmovies.data.model.Review
import com.icerrate.popularmovies.data.model.Trailer
import com.icerrate.popularmovies.data.model.TrailersResponse
import com.icerrate.popularmovies.data.source.MovieDataSource
import com.icerrate.popularmovies.provider.db.MovieDBHelper.Companion.BACKDROP_COLUMN
import com.icerrate.popularmovies.provider.db.MovieDBHelper.Companion.ID_COLUMN
import com.icerrate.popularmovies.provider.db.MovieDBHelper.Companion.POSTER_COLUMN
import com.icerrate.popularmovies.provider.db.MovieDBHelper.Companion.RELEASE_DATE_COLUMN
import com.icerrate.popularmovies.provider.db.MovieDBHelper.Companion.SYNOPSIS_COLUMN
import com.icerrate.popularmovies.provider.db.MovieDBHelper.Companion.TABLE_FAVORITE
import com.icerrate.popularmovies.provider.db.MovieDBHelper.Companion.TITLE_COLUMN
import com.icerrate.popularmovies.provider.db.MovieDBHelper.Companion.VOTE_AVERAGE_COLUMN
import com.icerrate.popularmovies.view.common.BaseCallback
import androidx.core.net.toUri

/**
 * @author Ivan Cerrate.
 */
class MovieLocalDataSource(private val context: Context) : MovieDataSource {

    companion object {
        private val stringUri = "content://${BuildConfig.APPLICATION_ID}.provider.db/$TABLE_FAVORITE"
        private val contentResolverUri: Uri = Uri.parse(stringUri)
    }

    override fun getPopularMovies(page: Int?, callback: BaseCallback<PaginatedResponse<Movie>>) {
        throw UnsupportedOperationException("Operation is not available!")
    }

    override fun getTopRatedMovies(page: Int?, callback: BaseCallback<PaginatedResponse<Movie>>) {
        throw UnsupportedOperationException("Operation is not available!")
    }

    override fun searchMovies(query: String, page: Int?, callback: BaseCallback<PaginatedResponse<Movie>>) {
        throw UnsupportedOperationException("Operation is not available!")
    }

    override fun getMovieTrailers(movieId: Int?, callback: BaseCallback<TrailersResponse<Trailer>>) {
        throw UnsupportedOperationException("Operation is not available!")
    }

    override fun getMovieReviews(movieId: Int?, callback: BaseCallback<PaginatedResponse<Review>>) {
        throw UnsupportedOperationException("Operation is not available!")
    }

    override fun getFavoriteMovies(callback: BaseCallback<ArrayList<Movie>>) {
        val moviesList = ArrayList<Movie>()
        val contentResolver: ContentResolver = context.contentResolver
        val columns = arrayOf(
            ID_COLUMN, TITLE_COLUMN, POSTER_COLUMN, BACKDROP_COLUMN,
            SYNOPSIS_COLUMN, VOTE_AVERAGE_COLUMN, RELEASE_DATE_COLUMN
        )
        val cursor: Cursor? = contentResolver.query(contentResolverUri, columns, null, null, null)
        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getInt(0)
                val title = it.getString(1) ?: ""
                val poster = it.getString(2) ?: ""
                val backdrop = it.getString(3) ?: ""
                val synopsis = it.getString(4) ?: ""
                val voteAverage = it.getDouble(5)
                val releaseDate = it.getString(6) ?: ""
                val movie = Movie(id = id, title = title, posterPath = poster, backdropPath = backdrop, overview = synopsis, voteAverage = voteAverage, releaseDate = releaseDate)
                moviesList.add(movie)
            }
        }
        callback.onSuccess(moviesList)
    }

    override fun isFavoriteMovie(movieId: Int, callback: BaseCallback<Boolean>) {
        val contentResolver: ContentResolver = context.contentResolver
        val columns = arrayOf(ID_COLUMN)
        val selection = "$ID_COLUMN=?"
        val selectionArgs = arrayOf(movieId.toString())
        val cursor: Cursor? = contentResolver.query(contentResolverUri, columns, selection, selectionArgs, null)
        val isFavorite = cursor?.use { it.count > 0 } ?: false
        callback.onSuccess(isFavorite)
    }

    override fun addFavoriteMovie(movie: Movie, callback: BaseCallback<Unit>) {
        val contentResolver: ContentResolver = context.contentResolver
        val values = ContentValues().apply {
            put(ID_COLUMN, movie.id)
            put(TITLE_COLUMN, movie.title)
            put(POSTER_COLUMN, movie.posterPath)
            put(BACKDROP_COLUMN, movie.backdropPath)
            put(SYNOPSIS_COLUMN, movie.overview)
            put(VOTE_AVERAGE_COLUMN, movie.voteAverage)
            put(RELEASE_DATE_COLUMN, movie.releaseDate)
        }
        contentResolver.insert(contentResolverUri, values)
        callback.onSuccess(Unit)
    }

    override fun removeFavoriteMovie(movieId: Int, callback: BaseCallback<Unit>) {
        val uri = "$stringUri/$movieId".toUri()
        val contentResolver: ContentResolver = context.contentResolver
        val where = "$ID_COLUMN=?"
        val selectionArgs = arrayOf(movieId.toString())
        contentResolver.delete(uri, where, selectionArgs)
        callback.onSuccess(Unit)
    }
}