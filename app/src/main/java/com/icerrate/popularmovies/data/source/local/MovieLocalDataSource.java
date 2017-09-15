package com.icerrate.popularmovies.data.source.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.icerrate.popularmovies.BuildConfig;
import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.PaginatedResponse;
import com.icerrate.popularmovies.data.model.Review;
import com.icerrate.popularmovies.data.model.Trailer;
import com.icerrate.popularmovies.data.model.TrailersResponse;
import com.icerrate.popularmovies.data.source.MovieDataSource;
import com.icerrate.popularmovies.view.common.BaseCallback;

import java.util.ArrayList;

import static com.icerrate.popularmovies.provider.db.MovieDBHelper.BACKDROP_COLUMN;
import static com.icerrate.popularmovies.provider.db.MovieDBHelper.ID_COLUMN;
import static com.icerrate.popularmovies.provider.db.MovieDBHelper.POSTER_COLUMN;
import static com.icerrate.popularmovies.provider.db.MovieDBHelper.RELEASE_DATE_COLUMN;
import static com.icerrate.popularmovies.provider.db.MovieDBHelper.SYNOPSIS_COLUMN;
import static com.icerrate.popularmovies.provider.db.MovieDBHelper.TABLE_FAVORITE;
import static com.icerrate.popularmovies.provider.db.MovieDBHelper.TITLE_COLUMN;
import static com.icerrate.popularmovies.provider.db.MovieDBHelper.VOTE_AVERAGE_COLUMN;

/**
 * @author Ivan Cerrate.
 */

public class MovieLocalDataSource implements MovieDataSource {

    private static final String stringUri = "content://"+ BuildConfig.APPLICATION_ID + ".provider.db/" + TABLE_FAVORITE;

    private static final Uri contentResolverUri = Uri.parse(stringUri);

    private Context context;

    public MovieLocalDataSource(Context context) {
        this.context = context;
    }

    @Override
    public void getPopularMovies(Integer page, BaseCallback<PaginatedResponse<Movie>> callback) {
        throw new UnsupportedOperationException("Operation is not available!");
    }

    @Override
    public void getTopRatedMovies(Integer page, BaseCallback<PaginatedResponse<Movie>> callback) {
        throw new UnsupportedOperationException("Operation is not available!");
    }

    @Override
    public void getMovieTrailers(Integer movieId, BaseCallback<TrailersResponse<Trailer>> callback) {
        throw new UnsupportedOperationException("Operation is not available!");
    }

    @Override
    public void getMovieReviews(Integer movieId, BaseCallback<PaginatedResponse<Review>> callback) {
        throw new UnsupportedOperationException("Operation is not available!");
    }

    @Override
    public void getFavoriteMovies(BaseCallback<ArrayList<Movie>> callback) {
        ArrayList<Movie> moviesList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        String[] columns = {ID_COLUMN, TITLE_COLUMN, POSTER_COLUMN, BACKDROP_COLUMN,
                SYNOPSIS_COLUMN, VOTE_AVERAGE_COLUMN, RELEASE_DATE_COLUMN};
        Cursor cursor = contentResolver.query(contentResolverUri, columns, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String poster = cursor.getString(2);
            String backdrop = cursor.getString(3);
            String synopsis = cursor.getString(4);
            double voteAverage = cursor.getDouble(5);
            String releaseDate = cursor.getString(6);
            Movie movie = new Movie(id, title, poster, backdrop, synopsis, voteAverage, releaseDate);
            moviesList.add(movie);
        }
        cursor.close();
        callback.onSuccess(moviesList);
    }

    @Override
    public void isFavoriteMovie(int movieId, BaseCallback<Boolean> callback) {
        boolean isFavorite = false;
        ContentResolver contentResolver = context.getContentResolver();
        String[] columns = {ID_COLUMN};
        String selection = ID_COLUMN + "=?";
        String[] selectionArgs = {String.valueOf(movieId)};
        Cursor cursor = contentResolver.query(contentResolverUri, columns, selection, selectionArgs, null);
        if (cursor != null && cursor.getCount() > 0) {
            isFavorite = true;
        }
        cursor.close();
        callback.onSuccess(isFavorite);
    }

    @Override
    public void addFavoriteMovie(Movie movie, BaseCallback<Void> callback) {
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, movie.getId());
        values.put(TITLE_COLUMN, movie.getTitle());
        values.put(POSTER_COLUMN, movie.getPosterPath());
        values.put(BACKDROP_COLUMN, movie.getBackdropPath());
        values.put(SYNOPSIS_COLUMN, movie.getOverview());
        values.put(VOTE_AVERAGE_COLUMN, movie.getVoteAverage());
        values.put(RELEASE_DATE_COLUMN, movie.getReleaseDate());
        contentResolver.insert(contentResolverUri, values);
        callback.onSuccess(null);
    }

    @Override
    public void removeFavoriteMovie(int movieId, BaseCallback<Void> callback) {
        Uri uri = Uri.parse(stringUri + "/" + movieId);
        ContentResolver contentResolver = context.getContentResolver();
        String where = ID_COLUMN + "=?";
        String[] selectionArgs = {String.valueOf(movieId)};
        contentResolver.delete(uri, where, selectionArgs);
        callback.onSuccess(null);
    }
}
