package com.icerrate.popularmovies.view.movies.detail;

import com.icerrate.popularmovies.data.model.Trailer;
import com.icerrate.popularmovies.view.common.BaseView;

import java.util.ArrayList;

/**
 * @author by Ivan Cerrate.
 */

public interface MovieDetailView extends BaseView {

    void showMovieDetail(String title, String releaseDate, String posterUrl, String backdropUrl, String rating, String synopsis);

    void showTrailers(ArrayList<Trailer> trailers);

    void resetTrailers();
}
