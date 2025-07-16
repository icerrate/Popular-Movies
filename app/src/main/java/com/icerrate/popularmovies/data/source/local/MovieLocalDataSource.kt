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
import androidx.core.net.toUri
import com.icerrate.popularmovies.data.model.Resource

/**
 * @author Ivan Cerrate.
 */
class MovieLocalDataSource(private val context: Context) : MovieDataSource {

    companion object {
        private const val STRING_URI = "content://${BuildConfig.APPLICATION_ID}.provider.db/$TABLE_FAVORITE"
        private val contentResolverUri: Uri = STRING_URI.toUri()
    }

    override suspend fun getPopularMovies(page: Int?): Resource<PaginatedResponse<Movie>> =
        throw UnsupportedOperationException("Operation is not available!")

    override suspend fun getTopRatedMovies(page: Int?): Resource<PaginatedResponse<Movie>> =
        throw UnsupportedOperationException("Operation is not available!")

    override suspend fun searchMovies(query: String, page: Int?): Resource<PaginatedResponse<Movie>> =
        throw UnsupportedOperationException("Operation is not available!")

    override suspend fun getMovieTrailers(movieId: Int?): Resource<TrailersResponse<Trailer>> =
        throw UnsupportedOperationException("Operation is not available!")

    override suspend fun getMovieReviews(movieId: Int?): Resource<PaginatedResponse<Review>> =
        throw UnsupportedOperationException("Operation is not available!")

    override suspend fun getFavoriteMovies(): Resource<ArrayList<Movie>> {
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
                val movie = Movie(
                    id = id,
                    title = title,
                    posterPath = poster,
                    backdropPath = backdrop,
                    overview = synopsis,
                    voteAverage = voteAverage,
                    releaseDate = releaseDate
                )
                moviesList.add(movie)
            }
        }
        return Resource.Success(moviesList)
    }

    override suspend fun isFavoriteMovie(movieId: Int): Resource<Boolean> {
        val contentResolver: ContentResolver = context.contentResolver
        val columns = arrayOf(ID_COLUMN)
        val selection = "$ID_COLUMN=?"
        val selectionArgs = arrayOf(movieId.toString())
        val cursor: Cursor? = contentResolver.query(contentResolverUri, columns, selection, selectionArgs, null)
        val isFavorite = cursor?.use { it.count > 0 } ?: false
        return Resource.Success(isFavorite)
    }

    override suspend fun addFavoriteMovie(movie: Movie): Resource<Unit> {
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
        return Resource.Success(Unit)
    }

    override suspend fun removeFavoriteMovie(movieId: Int) : Resource<Unit> {
        val uri = "$STRING_URI/$movieId".toUri()
        val contentResolver: ContentResolver = context.contentResolver
        val where = "$ID_COLUMN=?"
        val selectionArgs = arrayOf(movieId.toString())
        contentResolver.delete(uri, where, selectionArgs)
        return Resource.Success(Unit)
    }
}