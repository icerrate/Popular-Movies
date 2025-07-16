package com.icerrate.popularmovies.view.movies.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.icerrate.popularmovies.R
import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.data.model.Review
import com.icerrate.popularmovies.data.model.Trailer
import com.icerrate.popularmovies.databinding.FragmentMovieDetailBinding
import com.icerrate.popularmovies.view.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import com.icerrate.popularmovies.view.common.BaseItemDecoration
import androidx.core.net.toUri
import androidx.core.view.get

/**
 * @author Ivan Cerrate.
 */
@AndroidEntryPoint
class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>(
    FragmentMovieDetailBinding::inflate
), TrailersAdapter.OnItemClickListener, ReviewsAdapter.OnButtonClickListener, MenuProvider {

    private var shareMenuItem: MenuItem? = null
    private lateinit var trailersAdapter: TrailersAdapter
    private lateinit var reviewsAdapter: ReviewsAdapter
    private val viewModel: MovieDetailViewModel by viewModels()
    private var movieDetailFragmentListener: MovieDetailFragmentListener? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        castOrThrowException(context)
    }

    private fun castOrThrowException(context: Context) {
        try {
            movieDetailFragmentListener = context as MovieDetailFragmentListener
        } catch (_: ClassCastException) {
            throw ClassCastException("$context must implement MovieDetailFragmentListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        setupView()
        observeViewModel()
        
        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState)
        } else {
            val movie = arguments?.getParcelable<Movie>(KEY_MOVIE)
            movie?.let { viewModel.processIntent(MovieDetailIntent.SetMovieDetail(it)) }
        }
        viewModel.processIntent(MovieDetailIntent.LoadMovieDetails)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_movie_detail, menu)
        shareMenuItem = menu[0]
        viewModel.processIntent(MovieDetailIntent.ValidateMenu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return if (menuItem.itemId == R.id.share) {
            viewModel.processIntent(MovieDetailIntent.OnShareClick)
            true
        } else {
            false
        }
    }

    override fun saveInstanceState(outState: Bundle) {
        val currentState = viewModel.state.value
        outState.putParcelable(KEY_MOVIE, currentState?.movie)
        outState.putParcelableArrayList(KEY_TRAILERS, currentState?.trailers)
        outState.putParcelableArrayList(KEY_REVIEWS, currentState?.reviews)
    }

    override fun restoreInstanceState(savedState: Bundle) {
        val movie = savedState.getParcelable<Movie>(KEY_MOVIE)
        val trailers = savedState.getParcelableArrayList<Trailer>(KEY_TRAILERS)
        val reviews = savedState.getParcelableArrayList<Review>(KEY_REVIEWS)
        viewModel.processIntent(MovieDetailIntent.RestoreState(movie, trailers, reviews))
    }

    private fun setupView() {
        // Trailers
        trailersAdapter = TrailersAdapter(onItemClickListener = this)
        binding.trailers.adapter = trailersAdapter
        binding.trailers.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.trailers.addItemDecoration(
            BaseItemDecoration(ResourcesCompat.getDrawable(resources, R.drawable.horizontal_decorator, resources.newTheme()))
        )
        binding.trailers.isNestedScrollingEnabled = false
        
        // Reviews
        reviewsAdapter = ReviewsAdapter(onButtonClickListener = this)
        binding.reviews.adapter = reviewsAdapter
        binding.reviews.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.reviews.isNestedScrollingEnabled = false
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            handleState(state)
        }
    }

    private fun handleState(state: MovieDetailState) {
        // Handle movie header display
        state.movie?.let { movie ->
            showHeader(movie)
        }
        
        // Handle trailers
        if (state.trailers.isNotEmpty()) {
            trailersAdapter.addItems(state.trailers)
            binding.trailersNoData.visibility = View.INVISIBLE
        } else {
            binding.trailersNoData.visibility = View.VISIBLE
        }
        
        // Handle reviews
        if (state.reviews.isNotEmpty()) {
            reviewsAdapter.addItems(state.reviews)
            binding.reviewsNoData.visibility = View.INVISIBLE
        } else {
            binding.reviewsNoData.visibility = View.VISIBLE
        }
        
        // Handle share menu
        shareMenuItem?.isVisible = state.showShareMenu
        
        // Handle share trailer
        state.shareTrailerUrl?.let { trailerUrl ->
            prepareTrailerShare(trailerUrl)
            viewModel.onShareHandled()
        }
        
        // Handle errors
        state.errorMessage?.let { errorMessage ->
            showError(errorMessage)
            viewModel.onErrorHandled()
        }
    }

    private fun showHeader(movie: Movie) {
        movieDetailFragmentListener?.setBackdropImage(movie.getBackdropUrl(BACKDROP_CODE))
        movieDetailFragmentListener?.setCollapsingTitle(movie.title)
        movieDetailFragmentListener?.setFavoriteOnClickListener {
            viewModel.processIntent(MovieDetailIntent.OnFavoriteFabClicked)
        }
        movieDetailFragmentListener?.setFavoriteState(movie.isFavorite)
        
        binding.title.text = movie.title
        Glide.with(this)
            .load(movie.getPosterUrl(POSTER_CODE))
            .placeholder(resources.getDrawable(R.drawable.poster_placeholder))
            .into(binding.poster)
        binding.releaseDate.text = movie.releaseDate
        val rating = "%.1f".format(movie.voteAverage)
        binding.rating.text = getString(R.string.rating, rating)
        binding.synopsis.text = movie.overview
    }

    private fun prepareTrailerShare(trailerUrl: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, trailerUrl)
        }
        startActivity(Intent.createChooser(shareIntent, "Share Trailer link using"))
    }

    override fun onItemClick(view: View) {
        val trailer = view.tag as? Trailer
        trailer?.let {
            startActivity(Intent(Intent.ACTION_VIEW, it.getVideoUrl().toUri()))
        }
    }

    override fun onPlayButtonClick(view: View) {
        val review = view.tag as? Review
        review?.let {
            startActivity(Intent(Intent.ACTION_VIEW, it.url.toUri()))
        }
    }

    companion object {
        const val KEY_MOVIE = "MOVIE_KEY"
        const val KEY_TRAILERS = "TRAILERS_KEY"
        const val KEY_REVIEWS = "REVIEWS_KEY"

        private const val POSTER_CODE = "w500"
        private const val BACKDROP_CODE = "w780"

        fun newInstance(movie: Movie): MovieDetailFragment {
            val bundle = Bundle().apply {
                putParcelable(KEY_MOVIE, movie)
            }
            return MovieDetailFragment().apply {
                arguments = bundle
            }
        }
    }
}