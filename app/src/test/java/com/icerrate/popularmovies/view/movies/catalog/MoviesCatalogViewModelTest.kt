package com.icerrate.popularmovies.view.movies.catalog

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
class MoviesCatalogViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var movieRepository: MovieRepository

    private lateinit var viewModel: MoviesCatalogViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = MoviesCatalogViewModel(movieRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `processIntent LoadMoviesBySortType should update state with loading and sort type`() {
        val sortType = SortType.HIGHEST_RATED
        val intent = MoviesCatalogIntent.LoadMoviesBySortType(sortType)

        viewModel.processIntent(intent)

        val currentState = viewModel.state.value
        assert(currentState?.sortType == sortType)
        assert(currentState?.isLoading == true)
    }

    @Test
    fun `processIntent OnMovieItemClick should set navigation state when repository returns success`() = runTest {
        val movie = Movie(id = 1, title = "Test Movie", overview = "Test", posterPath = "", backdropPath = "", voteAverage = 7.5, releaseDate = "2023-01-01")
        val intent = MoviesCatalogIntent.OnMovieItemClick(movie)
        
        `when`(movieRepository.isFavoriteMovie(movie.id)).thenReturn(Resource.Success(true))

        viewModel.processIntent(intent)

        val currentState = viewModel.state.value
        assert(currentState?.navigateToMovieDetail?.id == movie.id)
        assert(currentState?.navigateToMovieDetail?.isFavorite == true)
    }

    @Test
    fun `processIntent OnMovieItemClick should set error state when repository returns error`() = runTest {
        val movie = Movie(id = 1, title = "Test Movie", overview = "Test", posterPath = "", backdropPath = "", voteAverage = 7.5, releaseDate = "2023-01-01")
        val intent = MoviesCatalogIntent.OnMovieItemClick(movie)
        val errorMessage = "Network error"
        
        `when`(movieRepository.isFavoriteMovie(movie.id)).thenReturn(Resource.Error(errorMessage))

        viewModel.processIntent(intent)

        val currentState = viewModel.state.value
        assert(currentState?.errorMessage == errorMessage)
    }
}