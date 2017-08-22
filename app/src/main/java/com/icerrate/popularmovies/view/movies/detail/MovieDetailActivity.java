package com.icerrate.popularmovies.view.movies.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.icerrate.popularmovies.R;
import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.view.common.BaseActivity;

import static com.icerrate.popularmovies.view.movies.detail.MovieDetailFragment.KEY_MOVIE;

/**
 * Created by Ivan Cerrate
 */

public class MovieDetailActivity extends BaseActivity {

    public static Intent makeIntent(Context context){
        return new Intent(context, MovieDetailActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setTitle(getString(R.string.title_activity_movie_detail));
        setNavigationToolbar(true);
        if (savedInstanceState == null) {
            Movie movie = getIntent().getParcelableExtra(KEY_MOVIE);
            MovieDetailFragment movieDetailFragment = MovieDetailFragment.newInstance(movie);
            replaceFragment(R.id.content, movieDetailFragment);
        }
    }
}
