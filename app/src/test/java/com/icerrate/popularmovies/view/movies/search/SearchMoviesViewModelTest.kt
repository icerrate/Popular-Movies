package com.icerrate.popularmovies.view.movies.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.data.model.PaginatedResponse
import com.icerrate.popularmovies.data.model.Resource
import com.icerrate.popularmovies.data.source.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class SearchMoviesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var movieRepository: MovieRepository

    private lateinit var viewModel: SearchMoviesViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = SearchMoviesViewModel(movieRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `processIntent SearchMovies with blank query should show search hint`() {
        val intent = SearchMoviesIntent.SearchMovies("")

        viewModel.processIntent(intent)

        val currentState = viewModel.state.value
        assert(currentState?.showSearchHint == true)
        assert(currentState?.showNoDataView == false)
        assert(currentState?.movies?.isEmpty() == true)
    }

    @Test
    fun `processIntent SearchMovies with valid query should update loading state`() = runTest {
        val query = "Avengers"
        val intent = SearchMoviesIntent.SearchMovies(query)
        val mockResponse = PaginatedResponse<Movie>()
        
        `when`(movieRepository.searchMovies(query, 1)).thenReturn(Resource.Success(mockResponse))

        viewModel.processIntent(intent)

        val currentState = viewModel.state.value
        assert(currentState?.query == query)
        assert(currentState?.showSearchHint == false)
    }

    @Test
    fun `processIntent OnMovieItemClick should set navigation state when repository returns success`() = runTest {
        val movie = Movie(id = 1, title = "Test Movie", overview = "Test", posterPath = "", backdropPath = "", voteAverage = 7.5, releaseDate = "2023-01-01")
        val intent = SearchMoviesIntent.OnMovieItemClick(movie)
        
        `when`(movieRepository.isFavoriteMovie(movie.id)).thenReturn(Resource.Success(false))

        viewModel.processIntent(intent)

        val currentState = viewModel.state.value
        assert(currentState?.navigateToMovieDetail?.id == movie.id)
        assert(currentState?.navigateToMovieDetail?.isFavorite == false)
    }
}