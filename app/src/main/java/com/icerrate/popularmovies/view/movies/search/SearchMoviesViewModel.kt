package com.icerrate.popularmovies.view.movies.search

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
class SearchMoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _state = MutableLiveData<SearchMoviesState>()
    val state: MutableLiveData<SearchMoviesState> = _state

    private var currentState: SearchMoviesState
        get() = _state.value ?: SearchMoviesState()
        set(value) {
            _state.value = value
        }

    fun processIntent(intent: SearchMoviesIntent) {
        when (intent) {
            is SearchMoviesIntent.SearchMovies -> searchMovies(intent.query)
            is SearchMoviesIntent.LoadNextMoviesPage -> loadNextMoviesPage()
            is SearchMoviesIntent.LoadMovies -> loadMovies()
            is SearchMoviesIntent.OnMovieItemClick -> onMovieItemClick(intent.movie)
            is SearchMoviesIntent.RestoreState -> restoreState(intent.query, intent.moviePaginatedResponse)
        }
    }

    private fun searchMovies(query: String) {
        if (query.isBlank()) {
            currentState = currentState.copy(
                showSearchHint = true,
                showNoDataView = false,
                movies = emptyList()
            )
            return
        }

        currentState = currentState.copy(
            query = query,
            isLoading = true,
            showSearchHint = false,
            paginatedResponse = PaginatedResponse()
        )

        performSearch(query, page = 1)
    }

    private fun loadNextMoviesPage() {
        val query = currentState.query
        val isLastPage = currentState.paginatedResponse.isLastPage
        
        if (query.isNullOrBlank() || isLastPage) {
            return
        }

        currentState = currentState.copy(isLoadingNextPage = true)
        
        val page = currentState.paginatedResponse.page?.let { it + 1 } ?: 1
        performSearch(query, page)
    }

    private fun loadMovies() {
        val query = currentState.query
        val paginatedResponse = currentState.paginatedResponse
        
        if (query.isNullOrBlank()) {
            currentState = currentState.copy(showSearchHint = true)
            return
        }

        if (paginatedResponse.page == 0) {
            searchMovies(query)
        } else {
            currentState = currentState.copy(
                movies = paginatedResponse.results,
                showSearchHint = false,
                showNoDataView = paginatedResponse.results.isEmpty()
            )
        }
    }

    private fun performSearch(query: String, page: Int) {
        viewModelScope.launch {
            when (val result = movieRepository.searchMovies(query, page)) {
                is Resource.Success -> {
                    handleSearchSuccess(result.value)
                }
                is Resource.Error -> {
                    currentState = currentState.copy(
                        isLoading = false,
                        isLoadingNextPage = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    private fun handleSearchSuccess(result: PaginatedResponse<Movie>) {
        val isFirstPage = result.page == 1
        val updatedPaginatedResponse = if (isFirstPage) {
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

        val movies = if (isFirstPage) {
            result.results
        } else {
            currentState.movies + result.results
        }

        currentState = currentState.copy(
            movies = movies,
            paginatedResponse = updatedPaginatedResponse,
            isLoading = false,
            isLoadingNextPage = false,
            showSearchHint = false,
            showNoDataView = movies.isEmpty(),
            errorMessage = null
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

    private fun restoreState(query: String?, moviePaginatedResponse: PaginatedResponse<Movie>?) {
        currentState = currentState.copy(
            query = query,
            paginatedResponse = moviePaginatedResponse ?: PaginatedResponse(),
            movies = moviePaginatedResponse?.results ?: emptyList(),
            showSearchHint = query.isNullOrBlank(),
            showNoDataView = moviePaginatedResponse?.results?.isEmpty() ?: false
        )
    }

    fun onNavigationHandled() {
        currentState = currentState.copy(navigateToMovieDetail = null)
    }

    fun onErrorHandled() {
        currentState = currentState.copy(errorMessage = null)
    }
}