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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.icerrate.popularmovies.databinding.FragmentMoviesCatalogBinding
import com.icerrate.popularmovies.R
import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.data.model.PaginatedResponse
import com.icerrate.popularmovies.utils.ViewUtils
import dagger.hilt.android.AndroidEntryPoint
import com.icerrate.popularmovies.view.common.BaseFragment
import com.icerrate.popularmovies.view.common.EndlessRecyclerOnScrollListener
import com.icerrate.popularmovies.view.movies.detail.MovieDetailActivity
import com.icerrate.popularmovies.view.movies.detail.MovieDetailFragment.Companion.KEY_MOVIE
import com.icerrate.popularmovies.view.movies.search.SearchMoviesActivity

/**
 * @author Ivan Cerrate.
 */
@AndroidEntryPoint
class MoviesCatalogFragment : BaseFragment<FragmentMoviesCatalogBinding>(
    FragmentMoviesCatalogBinding::inflate
), MoviesCatalogAdapter.OnItemClickListener, MenuProvider {

    private lateinit var adapter: MoviesCatalogAdapter
    private lateinit var endlessRecyclerOnScrollListener: EndlessRecyclerOnScrollListener
    private val viewModel: MoviesCatalogViewModel by viewModels()
    private var selectedMoviePoster: ImageView? = null
    
    private lateinit var movieDetailLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize the ActivityResultLauncher
        movieDetailLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.processIntent(MoviesCatalogIntent.OnBackFromDetail)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        setupView()
        observeViewModel()
        
        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState)
            viewModel.processIntent(MoviesCatalogIntent.LoadMovies)
        } else {
            viewModel.processIntent(MoviesCatalogIntent.LoadMoviesBySortType(SortType.MOST_POPULAR))
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_movies_catalog, menu)
        val currentState = viewModel.state.value
        when (currentState?.sortType) {
            SortType.MOST_POPULAR -> menu.findItem(R.id.popular).isChecked = true
            SortType.HIGHEST_RATED -> menu.findItem(R.id.top_rated).isChecked = true
            SortType.FAVORITE -> menu.findItem(R.id.favorite).isChecked = true
            null -> menu.findItem(R.id.popular).isChecked = true
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
        viewModel.processIntent(MoviesCatalogIntent.RefreshMoviesBySortType(selectedSortType))
        menuItem.isChecked = true
        return true
    }

    override fun saveInstanceState(outState: Bundle) {
        val currentState = viewModel.state.value
        outState.putSerializable(KEY_SORT_TYPE, currentState?.sortType ?: SortType.MOST_POPULAR)
        outState.putParcelable(KEY_PAGINATED_MOVIES, currentState?.paginatedResponse)
    }

    override fun restoreInstanceState(savedState: Bundle) {
        val sortType = savedState.getSerializable(KEY_SORT_TYPE) as SortType
        val moviesPaginatedResponse = savedState.getParcelable<PaginatedResponse<Movie>>(KEY_PAGINATED_MOVIES)
        viewModel.processIntent(MoviesCatalogIntent.RestoreState(sortType, moviesPaginatedResponse))
    }

    private fun setupView() {
        // Movies
        val columns = ViewUtils.getGridColumns(requireActivity())
        val gridLayoutManager = GridLayoutManager(context, columns)
        endlessRecyclerOnScrollListener = object : EndlessRecyclerOnScrollListener(gridLayoutManager) {
            override fun onLoadMore() {
                viewModel.processIntent(MoviesCatalogIntent.LoadNextMoviesPage)
            }
        }
        adapter = MoviesCatalogAdapter(onItemClickListener = this, columns = columns)
        binding.movies.adapter = adapter
        binding.movies.layoutManager = gridLayoutManager
        binding.movies.addOnScrollListener(endlessRecyclerOnScrollListener)
        
        // Refresh
        binding.refresh.apply {
            setOnRefreshListener { viewModel.processIntent(MoviesCatalogIntent.RefreshMovies) }
            setColorSchemeColors(resources.getColor(R.color.colorAccent))
        }
    }


    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            handleState(state)
        }
    }

    private fun handleState(state: MoviesCatalogState) {
        // Handle loading states
        binding.progress.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        binding.refresh.isRefreshing = state.isRefreshing
        binding.footerProgress.visibility = if (state.isLoadingNextPage) View.VISIBLE else View.GONE
        
        // Handle movies display
        if (state.movies.isNotEmpty()) {
            adapter.resetItems()
            adapter.addItems(ArrayList(state.movies))
            binding.moviesNoData.visibility = View.GONE
        } else {
            adapter.resetItems()
        }
        
        // Handle no data view
        if (state.showNoDataView) {
            binding.moviesNoData.visibility = View.VISIBLE
            binding.moviesNoData.text = getString(R.string.movies_no_data)
        } else {
            binding.moviesNoData.visibility = View.GONE
        }
        
        // Handle loading states for adapter
        adapter.setLoading(state.isLoadingNextPage)
        endlessRecyclerOnScrollListener.setLoading(state.isLoadingNextPage)
        
        // Handle navigation
        state.navigateToMovieDetail?.let { movie ->
            goToMovieDetail(movie)
            viewModel.onNavigationHandled()
        }
        
        // Handle errors
        state.errorMessage?.let { errorMessage ->
            showError(errorMessage)
            viewModel.onErrorHandled()
        }
    }

    private fun goToMovieDetail(movie: Movie) {
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
            viewModel.processIntent(MoviesCatalogIntent.OnMovieItemClick(it))
        }
    }


    companion object {
        const val KEY_SORT_TYPE = "SORT_TYPE_KEY"
        const val KEY_PAGINATED_MOVIES = "PAGINATED_MOVIES_KEY"

        fun newInstance(): MoviesCatalogFragment = MoviesCatalogFragment()
    }
}