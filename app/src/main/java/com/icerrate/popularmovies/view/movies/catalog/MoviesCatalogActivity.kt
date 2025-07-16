package com.icerrate.popularmovies.view.movies.catalog

import android.os.Bundle
import com.icerrate.popularmovies.R
import com.icerrate.popularmovies.databinding.ActivityMoviesCatalogBinding
import com.icerrate.popularmovies.view.common.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author Ivan Cerrate.
 */
@AndroidEntryPoint
class MoviesCatalogActivity : BaseActivity<ActivityMoviesCatalogBinding>(
    ActivityMoviesCatalogBinding::inflate
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = getString(R.string.title_activity_movies)
        setNavigationToolbar(false)
        if (savedInstanceState == null) {
            val moviesCatalogFragment = MoviesCatalogFragment.newInstance()
            replaceFragment(R.id.content, moviesCatalogFragment)
        }
    }
}