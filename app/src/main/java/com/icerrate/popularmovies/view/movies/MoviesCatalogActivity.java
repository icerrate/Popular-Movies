package com.icerrate.popularmovies.view.movies;

import android.os.Bundle;

import com.icerrate.popularmovies.R;
import com.icerrate.popularmovies.view.common.BaseActivity;

/**
 * @author Ivan Cerrate.
 */

public class MoviesCatalogActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_catalog);
        setTitle(getString(R.string.title_activity_movies));
        setNavigationToolbar(true);
        if (savedInstanceState == null) {
            MoviesCatalogFragment moviesCatalogFragment = MoviesCatalogFragment.newInstance();
            replaceFragment(R.id.content, moviesCatalogFragment);
        }
    }
}
