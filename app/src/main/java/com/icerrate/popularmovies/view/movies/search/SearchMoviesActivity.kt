package com.icerrate.popularmovies.view.movies.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import dagger.hilt.android.AndroidEntryPoint
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.icerrate.popularmovies.databinding.ActivitySearchMoviesBinding
import com.icerrate.popularmovies.R
import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.data.model.PaginatedResponse
import com.icerrate.popularmovies.utils.ViewUtils
import com.icerrate.popularmovies.view.common.BaseActivity
import com.icerrate.popularmovies.view.common.EndlessRecyclerOnScrollListener
import com.icerrate.popularmovies.view.movies.catalog.MoviesCatalogAdapter
import com.icerrate.popularmovies.view.movies.detail.MovieDetailActivity
import com.icerrate.popularmovies.view.movies.detail.MovieDetailFragment.Companion.KEY_MOVIE

/**
 * @author Ivan Cerrate.
 */
@AndroidEntryPoint
class SearchMoviesActivity : BaseActivity<ActivitySearchMoviesBinding>(
    ActivitySearchMoviesBinding::inflate
), MoviesCatalogAdapter.OnItemClickListener, MenuProvider {

    private var selectedMoviePoster: ImageView? = null
    private lateinit var adapter: MoviesCatalogAdapter
    private lateinit var endlessRecyclerOnScrollListener: EndlessRecyclerOnScrollListener
    private val viewModel: SearchMoviesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addMenuProvider(this, this, Lifecycle.State.RESUMED)

        setNavigationToolbar(true)
        setupView()
        setupSearchView()
        observeViewModel()
        
        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState)
            viewModel.processIntent(SearchMoviesIntent.LoadMovies)
            binding.searchView.post {
                binding.searchView.setQuery(viewModel.state.value?.query ?: "", false)
            }
        }
    }

    private fun setupView() {
        // Movies
        val columns = ViewUtils.getGridColumns(this)
        val gridLayoutManager = GridLayoutManager(this, columns)
        endlessRecyclerOnScrollListener = object : EndlessRecyclerOnScrollListener(gridLayoutManager) {
            override fun onLoadMore() {
                viewModel.processIntent(SearchMoviesIntent.LoadNextMoviesPage)
            }
        }
        adapter = MoviesCatalogAdapter(onItemClickListener = this, columns = columns)
        binding.movies.adapter = adapter
        binding.movies.layoutManager = gridLayoutManager
        binding.movies.addOnScrollListener(endlessRecyclerOnScrollListener)
    }

    private fun setupSearchView() {
        binding.searchView.onActionViewExpanded()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean = false

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.processIntent(SearchMoviesIntent.SearchMovies(newText))
                return false
            }
        })
    }

    private fun observeViewModel() {
        viewModel.state.observe(this) { state ->
            handleState(state)
        }
    }

    private fun handleState(state: SearchMoviesState) {
        // Handle loading
        binding.progress.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        
        // Handle movies
        if (state.movies.isNotEmpty()) {
            adapter.resetItems()
            adapter.addItems(state.movies)
            binding.moviesNoData.visibility = View.GONE
        } else {
            adapter.resetItems()
        }
        
        // Handle search hint
        if (state.showSearchHint) {
            binding.moviesNoData.visibility = View.VISIBLE
            binding.moviesNoData.text = getString(R.string.search_movies_hint)
        } else if (state.showNoDataView) {
            binding.moviesNoData.visibility = View.VISIBLE
            binding.moviesNoData.text = getString(R.string.movies_no_data)
        } else {
            binding.moviesNoData.visibility = View.GONE
        }
        
        // Handle loading states
        adapter.setLoading(state.isLoadingNextPage)
        binding.footerProgress.visibility = if (state.isLoadingNextPage) View.VISIBLE else View.GONE
        endlessRecyclerOnScrollListener.setLoading(state.isLoadingNextPage)
        
        // Handle navigation
        state.navigateToMovieDetail?.let { movie ->
            goToMovieDetail(movie)
            viewModel.onNavigationHandled()
        }
        
        // Handle errors
        state.errorMessage?.let { errorMessage ->
            ViewUtils.createSnackbar(binding.toolbar, errorMessage, Snackbar.LENGTH_SHORT).show()
            viewModel.onErrorHandled()
        }
    }

    private fun goToMovieDetail(movie: Movie) {
        selectedMoviePoster?.let { poster ->
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                poster,
                ViewCompat.getTransitionName(poster) ?: ""
            )
            startActivity(
                MovieDetailActivity.makeIntent(this).putExtra(KEY_MOVIE, movie),
                options.toBundle()
            )
        }
        overridePendingTransition(R.anim.fade_in, R.anim.none)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        // Menu is empty for this activity
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }

    override fun saveInstanceState(outState: Bundle) {
        val currentState = viewModel.state.value
        outState.putString(KEY_SEARCH_QUERY, currentState?.query)
        outState.putParcelable(KEY_PAGINATED_MOVIES, currentState?.paginatedResponse)
    }

    override fun restoreInstanceState(savedState: Bundle) {
        val query = savedState.getString(KEY_SEARCH_QUERY)
        val moviesPaginatedResponse = savedState.getParcelable<PaginatedResponse<Movie>>(KEY_PAGINATED_MOVIES)
        viewModel.processIntent(SearchMoviesIntent.RestoreState(query, moviesPaginatedResponse))
    }


    override fun onItemClick(view: View) {
        val movie = view.tag as? Movie
        movie?.let {
            selectedMoviePoster = view.findViewById(R.id.poster)
            viewModel.processIntent(SearchMoviesIntent.OnMovieItemClick(it))
        }
    }

    companion object {
        const val KEY_SEARCH_QUERY = "SEARCH_QUERY_KEY"
        const val KEY_PAGINATED_MOVIES = "PAGINATED_MOVIES_KEY"

        fun makeIntent(context: Context): Intent = Intent(context, SearchMoviesActivity::class.java)
    }
}