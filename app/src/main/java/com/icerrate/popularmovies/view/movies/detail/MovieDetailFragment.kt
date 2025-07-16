package com.icerrate.popularmovies.view.movies.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
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
import com.icerrate.popularmovies.data.source.MovieRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.icerrate.popularmovies.view.common.BaseItemDecoration
import androidx.core.net.toUri

/**
 * @author Ivan Cerrate.
 */
@AndroidEntryPoint
class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>(
    FragmentMovieDetailBinding::inflate
), MovieDetailContract.View,
    TrailersAdapter.OnItemClickListener, ReviewsAdapter.OnButtonClickListener, MenuProvider {

    private var shareMenuItem: MenuItem? = null
    private lateinit var trailersAdapter: TrailersAdapter
    private lateinit var reviewsAdapter: ReviewsAdapter
    private lateinit var presenter: MovieDetailPresenter
    private var movieDetailFragmentListener: MovieDetailFragmentListener? = null
    
    @Inject
    lateinit var movieRepository: MovieRepository


    override fun onAttach(context: Context) {
        super.onAttach(context)
        castOrThrowException(context)
    }

    private fun castOrThrowException(context: Context) {
        try {
            movieDetailFragmentListener = context as MovieDetailFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement MovieDetailFragmentListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = MovieDetailPresenter(
            this,
            movieRepository
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        setupView()
        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState)
        } else {
            val movie = arguments?.getParcelable<Movie>(KEY_MOVIE)
            movie?.let { presenter.setMovieDetail(it) }
        }
        presenter.loadMovieDetails()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_movie_detail, menu)
        shareMenuItem = menu.getItem(0)
        presenter.validateMenu()
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return if (menuItem.itemId == R.id.share) {
            presenter.onShareClick()
            true
        } else {
            false
        }
    }

    override fun saveInstanceState(outState: Bundle) {
        outState.putParcelable(KEY_MOVIE, presenter.getMovie())
        outState.putParcelableArrayList(KEY_TRAILERS, presenter.getTrailers())
        outState.putParcelableArrayList(KEY_REVIEWS, presenter.getReviews())
    }

    override fun restoreInstanceState(savedState: Bundle) {
        val movie = savedState.getParcelable<Movie>(KEY_MOVIE)
        val trailers = savedState.getParcelableArrayList<Trailer>(KEY_TRAILERS)
        val reviews = savedState.getParcelableArrayList<Review>(KEY_REVIEWS)
        presenter.loadPresenterState(movie, trailers, reviews)
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

    override fun showHeader(
        title: String,
        releaseDate: String,
        posterUrl: String,
        backdropUrl: String,
        rating: String,
        synopsis: String
    ) {
        movieDetailFragmentListener?.setBackdropImage(backdropUrl)
        movieDetailFragmentListener?.setCollapsingTitle(title)
        movieDetailFragmentListener?.setFavoriteOnClickListener {
            presenter.onFavoriteFabClicked()
        }
        
        binding.title.text = title
        Glide.with(this)
            .load(posterUrl)
            .placeholder(resources.getDrawable(R.drawable.poster_placeholder))
            .into(binding.poster)
        binding.releaseDate.text = releaseDate
        binding.rating.text = getString(R.string.rating, rating)
        binding.synopsis.text = synopsis
    }

    override fun showTrailers(trailers: ArrayList<Trailer>) {
        trailersAdapter.addItems(trailers)
    }

    override fun showTrailersNoData(show: Boolean) {
        binding.trailersNoData.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    override fun showReviews(reviews: ArrayList<Review>) {
        reviewsAdapter.addItems(reviews)
    }

    override fun showReviewsNoData(show: Boolean) {
        binding.reviewsNoData.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    override fun showFavoriteState(isFavorite: Boolean) {
        movieDetailFragmentListener?.setFavoriteState(isFavorite)
    }

    override fun updateFavoriteState(isFavorite: Boolean) {
        movieDetailFragmentListener?.updateFavoriteState(isFavorite)
    }

    override fun prepareTrailerShare(trailerUrl: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, trailerUrl)
        }
        startActivity(Intent.createChooser(shareIntent, "Share Trailer link using"))
    }

    override fun showShareMenu(show: Boolean) {
        shareMenuItem?.isVisible = show
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