package com.icerrate.popularmovies.view.movies.catalog

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.icerrate.popularmovies.databinding.FragmentMoviesCatalogBinding
import com.icerrate.popularmovies.R
import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.data.model.PaginatedResponse
import com.icerrate.popularmovies.utils.ViewUtils
import com.icerrate.popularmovies.data.source.MovieRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.icerrate.popularmovies.view.common.BaseFragment
import com.icerrate.popularmovies.view.common.EndlessRecyclerOnScrollListener
import com.icerrate.popularmovies.view.movies.catalog.MoviesCatalogPresenter.SortType
import com.icerrate.popularmovies.view.movies.detail.MovieDetailActivity
import com.icerrate.popularmovies.view.movies.detail.MovieDetailFragment.Companion.KEY_MOVIE
import com.icerrate.popularmovies.view.movies.search.SearchMoviesActivity

/**
 * @author Ivan Cerrate.
 */
@AndroidEntryPoint
class MoviesCatalogFragment : BaseFragment<FragmentMoviesCatalogBinding>(
    FragmentMoviesCatalogBinding::inflate
), MoviesCatalogContract.View,
    MoviesCatalogAdapter.OnItemClickListener, MenuProvider {

    private lateinit var adapter: MoviesCatalogAdapter
    private lateinit var endlessRecyclerOnScrollListener: EndlessRecyclerOnScrollListener
    private lateinit var presenter: MoviesCatalogPresenter
    private var selectedMoviePoster: ImageView? = null
    
    private lateinit var movieDetailLauncher: ActivityResultLauncher<Intent>
    
    @Inject
    lateinit var movieRepository: MovieRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = MoviesCatalogPresenter(
            this,
            movieRepository
        )
        
        // Initialize the ActivityResultLauncher
        movieDetailLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                presenter.onBackFromDetail()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        setupView()
        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState)
            presenter.loadMovies()
        } else {
            presenter.loadMoviesBySortType(SortType.MOST_POPULAR)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_movies_catalog, menu)
        val currentSortType = presenter.getSortType()
        when (currentSortType) {
            SortType.MOST_POPULAR -> menu.findItem(R.id.popular).isChecked = true
            SortType.HIGHEST_RATED -> menu.findItem(R.id.top_rated).isChecked = true
            SortType.FAVORITE -> menu.findItem(R.id.favorite).isChecked = true
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        val selectedSortType = when (menuItem.itemId) {
            R.id.popular -> SortType.MOST_POPULAR
            R.id.top_rated -> SortType.HIGHEST_RATED
            R.id.favorite -> SortType.FAVORITE
            R.id.action_search -> {
                startActivity(SearchMoviesActivity.makeIntent(requireContext()))
                activity?.overridePendingTransition(R.anim.fade_in, R.anim.none)
                return true
            }
            else -> return false
        }
        presenter.refreshMoviesBySortType(selectedSortType)
        menuItem.isChecked = true
        return true
    }

    override fun saveInstanceState(outState: Bundle) {
        outState.putSerializable(KEY_SORT_TYPE, presenter.getSortType())
        outState.putParcelable(KEY_PAGINATED_MOVIES, presenter.getMoviesPaginatedResponse())
    }

    override fun restoreInstanceState(savedState: Bundle) {
        val sortType = savedState.getSerializable(KEY_SORT_TYPE) as SortType
        val moviesPaginatedResponse = savedState.getParcelable<PaginatedResponse<Movie>>(KEY_PAGINATED_MOVIES)
        presenter.loadPresenterState(sortType, moviesPaginatedResponse)
    }

    private fun setupView() {
        // Movies
        val columns = ViewUtils.getGridColumns(requireActivity())
        val gridLayoutManager = GridLayoutManager(context, columns)
        endlessRecyclerOnScrollListener = object : EndlessRecyclerOnScrollListener(gridLayoutManager) {
            override fun onLoadMore() {
                presenter.loadNextMoviesPage()
            }
        }
        adapter = MoviesCatalogAdapter(onItemClickListener = this, columns = columns)
        binding.movies.adapter = adapter
        binding.movies.layoutManager = gridLayoutManager
        binding.movies.addOnScrollListener(endlessRecyclerOnScrollListener)
        
        // Refresh
        binding.refresh.apply {
            setOnRefreshListener { presenter.refreshMovies() }
            setColorSchemeColors(resources.getColor(R.color.colorAccent))
        }
    }


    override fun resetMovies() {
        adapter.resetItems()
        adapter.setLoading(false)
        endlessRecyclerOnScrollListener.setLoading(false)
    }

    override fun showMovies(movies: ArrayList<Movie>) {
        adapter.addItems(movies)
        endlessRecyclerOnScrollListener.setLoading(false)
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
                requireActivity(),
                poster,
                getString(R.string.movie_poster_transition)
            )
            val intent = MovieDetailActivity.makeIntent(requireActivity()).putExtra(KEY_MOVIE, movie)
            movieDetailLauncher.launch(intent, options)
        }
        activity?.overridePendingTransition(R.anim.fade_in, R.anim.none)
    }

    override fun onItemClick(view: View) {
        val movie = view.tag as? Movie
        movie?.let {
            selectedMoviePoster = view.findViewById(R.id.poster)
            presenter.onMovieItemClick(it)
        }
    }

    override fun showProgressBar(show: Boolean) {
        binding.progress.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showRefreshLayout(show: Boolean) {
        binding.refresh.isRefreshing = show
    }

    companion object {
        const val KEY_SORT_TYPE = "SORT_TYPE_KEY"
        const val KEY_PAGINATED_MOVIES = "PAGINATED_MOVIES_KEY"

        fun newInstance(): MoviesCatalogFragment = MoviesCatalogFragment()
    }
}