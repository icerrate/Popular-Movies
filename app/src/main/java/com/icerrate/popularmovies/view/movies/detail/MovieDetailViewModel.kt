package com.icerrate.popularmovies.view.movies.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.data.model.Resource
import com.icerrate.popularmovies.data.model.Review
import com.icerrate.popularmovies.data.model.Trailer
import com.icerrate.popularmovies.data.source.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _state = MutableLiveData<MovieDetailState>()
    val state: MutableLiveData<MovieDetailState> = _state

    private var currentState: MovieDetailState
        get() = _state.value ?: MovieDetailState()
        set(value) {
            _state.value = value
        }

    fun processIntent(intent: MovieDetailIntent) {
        when (intent) {
            is MovieDetailIntent.SetMovieDetail -> setMovieDetail(intent.movie)
            is MovieDetailIntent.LoadMovieDetails -> loadMovieDetails()
            is MovieDetailIntent.OnFavoriteFabClicked -> onFavoriteFabClicked()
            is MovieDetailIntent.OnShareClick -> onShareClick()
            is MovieDetailIntent.ValidateMenu -> validateMenu()
            is MovieDetailIntent.RestoreState -> restoreState(intent.movie, intent.trailers, intent.reviews)
        }
    }

    private fun setMovieDetail(movie: Movie) {
        currentState = currentState.copy(movie = movie)
    }

    private fun loadMovieDetails() {
        val movie = currentState.movie ?: return
        
        currentState = currentState.copy(isLoading = true)
        
        viewModelScope.launch {
            // Load trailers and reviews concurrently
            currentState = when (val trailers = movieRepository.getMovieTrailers(movie.id)) {
                is Resource.Success -> {
                    currentState.copy(
                        trailers = trailers.value.results,
                        showShareMenu = trailers.value.results.isNotEmpty(),
                        isLoading = false
                    )
                }

                is Resource.Error -> {
                    currentState.copy(
                        errorMessage = trailers.message,
                        isLoading = false
                    )
                }
            }

            currentState = when (val reviews = movieRepository.getMovieReviews(movie.id)) {
                is Resource.Success -> {
                    currentState.copy(
                        reviews = reviews.value.results,
                        isLoading = false
                    )
                }

                is Resource.Error -> {
                    currentState.copy(
                        errorMessage = reviews.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun onFavoriteFabClicked() {
        val movie = currentState.movie ?: return
        
        viewModelScope.launch {
            try {
                val result = if (movie.isFavorite) {
                    movieRepository.removeFavoriteMovie(movie.id)
                } else {
                    movieRepository.addFavoriteMovie(movie)
                }

                currentState = currentState.copy(
                    movie = movie.copy(
                        isFavorite = movie.isFavorite.not()
                    )
                )
            } catch (exception: Exception) {
                currentState = currentState.copy(errorMessage = exception.message)
            }
        }
    }

    private fun onShareClick() {
        val trailers = currentState.trailers
        if (trailers.isNotEmpty()) {
            val trailer = trailers[0]
            val trailerUrl = "https://www.youtube.com/watch?v=${trailer.key}"
            currentState = currentState.copy(shareTrailerUrl = trailerUrl)
        }
    }

    private fun validateMenu() {
        val hasTrailers = currentState.trailers.isNotEmpty()
        currentState = currentState.copy(showShareMenu = hasTrailers)
    }

    private fun restoreState(movie: Movie?, trailers: ArrayList<Trailer>?, reviews: ArrayList<Review>?) {
        currentState = currentState.copy(
            movie = movie,
            trailers = trailers ?: arrayListOf(),
            reviews = reviews ?: arrayListOf(),
            showShareMenu = trailers?.isNotEmpty() ?: false
        )
    }

    fun onShareHandled() {
        currentState = currentState.copy(shareTrailerUrl = null)
    }

    fun onErrorHandled() {
        currentState = currentState.copy(errorMessage = null)
    }
}