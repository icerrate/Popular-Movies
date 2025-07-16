package com.icerrate.popularmovies.view.movies.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
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
import com.icerrate.popularmovies.data.source.MovieRepository
import javax.inject.Inject
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
), SearchMoviesContract.View,
    MoviesCatalogAdapter.OnItemClickListener, MenuProvider {

    private var selectedMoviePoster: ImageView? = null
    private lateinit var adapter: MoviesCatalogAdapter
    private lateinit var endlessRecyclerOnScrollListener: EndlessRecyclerOnScrollListener
    private lateinit var presenter: SearchMoviesPresenter
    
    @Inject
    lateinit var movieRepository: MovieRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addMenuProvider(this, this, Lifecycle.State.RESUMED)

        setNavigationToolbar(true)
        presenter = SearchMoviesPresenter(
            this,
            movieRepository
        )
        setupView()
        setupSearchView()
        
        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState)
            presenter.loadMovies()
            binding.searchView.post {
                binding.searchView.setQuery(presenter.getQuery(), false)
            }
        }
    }

    private fun setupView() {
        // Movies
        val columns = ViewUtils.getGridColumns(this)
        val gridLayoutManager = GridLayoutManager(this, columns)
        endlessRecyclerOnScrollListener = object : EndlessRecyclerOnScrollListener(gridLayoutManager) {
            override fun onLoadMore() {
                presenter.loadNextMoviesPage()
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
                presenter.searchMovies(newText)
                return false
            }
        })
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        // Menu is empty for this activity
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }

    override fun saveInstanceState(outState: Bundle) {
        outState.putString(KEY_SEARCH_QUERY, presenter.getQuery())
        outState.putParcelable(KEY_PAGINATED_MOVIES, presenter.getMoviesPaginatedResponse())
    }

    override fun restoreInstanceState(savedState: Bundle) {
        val query = savedState.getString(KEY_SEARCH_QUERY)
        val moviesPaginatedResponse = savedState.getParcelable<PaginatedResponse<Movie>>(KEY_PAGINATED_MOVIES)
        presenter.loadPresenterState(query, moviesPaginatedResponse)
    }

    override fun showProgressBar(show: Boolean) {
        binding.progress.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showError(errorMessage: String) {
        ViewUtils.createSnackbar(binding.toolbar, errorMessage, Snackbar.LENGTH_SHORT).show()
    }

    override fun resetMovies() {
        adapter.resetItems()
        adapter.setLoading(false)
        endlessRecyclerOnScrollListener.setLoading(false)
    }

    override fun showMovies(movies: List<Movie>) {
        adapter.addItems(movies)
        endlessRecyclerOnScrollListener.setLoading(false)
    }

    override fun showSearchHint(show: Boolean) {
        binding.moviesNoData.visibility = if (show) View.VISIBLE else View.GONE
        binding.moviesNoData.text = getString(R.string.search_movies_hint)
    }

    override fun showNoDataView(show: Boolean) {
        binding.moviesNoData.visibility = if (show) View.VISIBLE else View.GONE
        binding.moviesNoData.text = getString(R.string.movies_no_data)
    }

    override fun showFooterProgress(show: Boolean) {
        adapter.setLoading(show)
        binding.footerProgress.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun goToMovieDetail(movie: Movie) {
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

    override fun onItemClick(view: View) {
        val movie = view.tag as? Movie
        movie?.let {
            selectedMoviePoster = view.findViewById(R.id.poster)
            presenter.onMovieItemClick(it)
        }
    }

    companion object {
        const val KEY_ACTIVE_SEARCH = "ACTIVE_SEARCH_KEY"
        const val KEY_SEARCH_QUERY = "SEARCH_QUERY_KEY"
        const val KEY_PAGINATED_MOVIES = "PAGINATED_MOVIES_KEY"

        fun makeIntent(context: Context): Intent = Intent(context, SearchMoviesActivity::class.java)
    }
}