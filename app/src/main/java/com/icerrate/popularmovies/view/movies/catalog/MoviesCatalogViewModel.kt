package com.icerrate.popularmovies.view.movies.catalog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.data.model.PaginatedResponse
import com.icerrate.popularmovies.data.model.Resource
import com.icerrate.popularmovies.data.source.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesCatalogViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _state = MutableLiveData<MoviesCatalogState>()
    val state: MutableLiveData<MoviesCatalogState> = _state

    private var currentState: MoviesCatalogState
        get() = _state.value ?: MoviesCatalogState()
        set(value) {
            _state.value = value
        }

    fun processIntent(intent: MoviesCatalogIntent) {
        when (intent) {
            is MoviesCatalogIntent.LoadMovies -> loadMovies()
            is MoviesCatalogIntent.LoadMoviesBySortType -> loadMoviesBySortType(intent.sortType)
            is MoviesCatalogIntent.LoadNextMoviesPage -> loadNextMoviesPage()
            is MoviesCatalogIntent.RefreshMovies -> refreshMovies()
            is MoviesCatalogIntent.RefreshMoviesBySortType -> refreshMoviesBySortType(intent.sortType)
            is MoviesCatalogIntent.OnBackFromDetail -> onBackFromDetail()
            is MoviesCatalogIntent.OnMovieItemClick -> onMovieItemClick(intent.movie)
            is MoviesCatalogIntent.RestoreState -> restoreState(intent.sortType, intent.moviesResponse)
        }
    }

    private fun loadMovies() {
        val currentPaginatedResponse = currentState.paginatedResponse
        if (currentPaginatedResponse.page == 0) {
            refreshMovies()
        } else {
            currentState = currentState.copy(
                movies = currentPaginatedResponse.results
            )
        }
    }

    private fun loadMoviesBySortType(sortType: SortType) {
        currentState = currentState.copy(
            sortType = sortType,
            isLoading = true
        )
        loadMovies()
    }

    private fun loadNextMoviesPage() {
        val isLastPage = currentState.paginatedResponse.isLastPage
        if (isLastPage) {
            return
        }
        
        currentState = currentState.copy(isLoadingNextPage = true)
        getMovies(force = false)
    }

    private fun refreshMovies() {
        currentState = currentState.copy(
            paginatedResponse = PaginatedResponse(),
            isRefreshing = true
        )
        getMovies(force = true)
    }

    private fun refreshMoviesBySortType(sortType: SortType) {
        currentState = currentState.copy(
            sortType = sortType,
            paginatedResponse = PaginatedResponse(),
            movies = emptyList(),
            isLoading = true
        )
        getMovies(force = true)
    }

    private fun onBackFromDetail() {
        if (currentState.sortType == SortType.FAVORITE) {
            refreshMovies()
        }
    }

    private fun getMovies(force: Boolean) {
        when (currentState.sortType) {
            SortType.MOST_POPULAR -> getPopularMovies(force)
            SortType.HIGHEST_RATED -> getTopRatedMovies(force)
            SortType.FAVORITE -> getFavoriteMovies(force)
        }
    }

    private fun getPopularMovies(force: Boolean) {
        val page = currentState.paginatedResponse.page?.let { it + 1 } ?: 1
        val currentPage = if (force) 1 else page
        
        viewModelScope.launch {
            when (val result = movieRepository.getPopularMovies(currentPage)) {
                is Resource.Success -> handleMoviesSuccess(result.value, force)
                is Resource.Error -> handleMoviesFailure(result.message)
            }
        }
    }

    private fun getTopRatedMovies(force: Boolean) {
        val page = currentState.paginatedResponse.page?.let { it + 1 } ?: 1
        val currentPage = if (force) 1 else page

        viewModelScope.launch {
            when (val result = movieRepository.getTopRatedMovies(currentPage)) {
                is Resource.Success -> handleMoviesSuccess(result.value, force)
                is Resource.Error -> handleMoviesFailure(result.message)
            }
        }
    }

    private fun getFavoriteMovies(force: Boolean) {
        viewModelScope.launch {
            when (val result = movieRepository.getFavoriteMovies()) {
                is Resource.Success -> {
                    val paginatedResponse = PaginatedResponse<Movie>().apply {
                        setMeta(
                            page = PAGINATION,
                            totalResults = result.value.size,
                            totalPages = PAGINATION
                        )
                        results.addAll(result.value)
                    }
                    handleMoviesSuccess(paginatedResponse, force)
                }
                is Resource.Error -> handleMoviesFailure(result.message)
            }
        }
    }

    private fun handleMoviesSuccess(result: PaginatedResponse<Movie>, force: Boolean) {
        val newPaginatedResponse = if (force) {
            result
        } else {
            currentState.paginatedResponse.apply {
                setMeta(
                    page = result.page,
                    totalResults = result.totalResults,
                    totalPages = result.totalPages
                )
                results.addAll(result.results)
            }
        }

        val movies = if (force) {
            result.results
        } else {
            currentState.movies + result.results
        }

        currentState = currentState.copy(
            isLoading = false,
            isRefreshing = false,
            isLoadingNextPage = false,
            movies = movies,
            paginatedResponse = newPaginatedResponse,
            showNoDataView = movies.isEmpty(),
            errorMessage = null
        )
    }

    private fun handleMoviesFailure(errorMessage: String) {
        currentState = currentState.copy(
            isLoading = false,
            isRefreshing = false,
            isLoadingNextPage = false,
            errorMessage = errorMessage
        )
    }

    private fun onMovieItemClick(movie: Movie) {
        viewModelScope.launch {
            when (val result = movieRepository.isFavoriteMovie(movie.id)) {
                is Resource.Success -> {
                    movie.isFavorite = result.value
                    currentState = currentState.copy(navigateToMovieDetail = movie)
                }
                is Resource.Error -> {
                    currentState = currentState.copy(errorMessage = result.message)
                }
            }
        }
    }

    private fun restoreState(sortType: SortType, moviesResponse: PaginatedResponse<Movie>?) {
        currentState = currentState.copy(
            sortType = sortType,
            paginatedResponse = moviesResponse ?: PaginatedResponse(),
            movies = moviesResponse?.results ?: emptyList()
        )
    }

    fun onNavigationHandled() {
        currentState = currentState.copy(navigateToMovieDetail = null)
    }

    fun onErrorHandled() {
        currentState = currentState.copy(errorMessage = null)
    }

    companion object {
        private const val PAGINATION = 1
    }
}