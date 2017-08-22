package com.icerrate.popularmovies.view.movies.detail;

import com.icerrate.popularmovies.R;
import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.source.MovieDataSource;
import com.icerrate.popularmovies.utils.FormatUtils;
import com.icerrate.popularmovies.view.common.BasePresenter;

/**
 * Created by Ivan Cerrate.
 */

public class MovieDetailPresenter extends BasePresenter<MovieDetailView> {

    private Movie movie;

    private MovieDataSource movieDataSource;

    public MovieDetailPresenter(MovieDetailView view, MovieDataSource movieDataSource) {
        super(view);
        this.movieDataSource = movieDataSource;
    }

    public void setMovieDetail(Movie movie) {
        this.movie = movie;
    }

    public void loadMovie() {
        String title = movie.getTitle();
        String releaseDate = FormatUtils.formatDate(movie.getReleaseDate(), FormatUtils.FORMAT_yyyy_MM_dd, FormatUtils.FORMAT_MMM_dd_yyyy);
        String posterUrl = movie.getPosterUrl("w185");
        String rating = movie.getVoteAverage() + getStringRes(R.string.rating);
        String synopsis = movie.getOverview();
        view.showMovieDetail(title, releaseDate, posterUrl, rating, synopsis);
    }

    public Movie getMovie() {
        return movie;
    }

    public void loadPresenterState(Movie movie) {
        this.movie = movie;
    }
}
