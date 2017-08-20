package com.icerrate.popularmovies.view.catalog;

import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.view.common.BaseView;

import java.util.ArrayList;

/**
 * Created by Ivan on 13/11/2016.
 */

public interface MoviesCatalogView extends BaseView {

    void resetMovies();
    void showMovies(ArrayList<Movie> movies);
}
