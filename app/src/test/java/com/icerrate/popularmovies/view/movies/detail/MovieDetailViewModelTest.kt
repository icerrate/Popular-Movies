package com.icerrate.popularmovies.view.movies.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.data.model.Resource
import com.icerrate.popularmovies.data.model.Trailer
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
class MovieDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var movieRepository: MovieRepository

    private lateinit var viewModel: MovieDetailViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = MovieDetailViewModel(movieRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `processIntent SetMovieDetail should update state with movie`() {
        val movie = Movie(id = 1, title = "Test Movie", overview = "Test", posterPath = "", backdropPath = "", voteAverage = 7.5, releaseDate = "2023-01-01")
        val intent = MovieDetailIntent.SetMovieDetail(movie)

        viewModel.processIntent(intent)

        val currentState = viewModel.state.value
        assert(currentState?.movie?.id == movie.id)
        assert(currentState?.movie?.title == movie.title)
    }

    @Test
    fun `processIntent OnFavoriteFabClicked should toggle favorite status when movie is not favorite`() = runTest {
        val movie = Movie(id = 1, title = "Test Movie", overview = "Test", posterPath = "", backdropPath = "", voteAverage = 7.5, releaseDate = "2023-01-01", isFavorite = false)
        
        viewModel.processIntent(MovieDetailIntent.SetMovieDetail(movie))
        `when`(movieRepository.addFavoriteMovie(movie)).thenReturn(Resource.Success(Unit))

        viewModel.processIntent(MovieDetailIntent.OnFavoriteFabClicked)

        val currentState = viewModel.state.value
        assert(currentState?.movie?.isFavorite == true)
    }

    @Test
    fun `processIntent OnShareClick should set share trailer URL when trailers exist`() {
        val trailer = Trailer(id = "1", key = "testKey", name = "Test Trailer", site = "YouTube", type = "Trailer")
        val trailers = arrayListOf(trailer)
        
        viewModel.processIntent(MovieDetailIntent.RestoreState(null, trailers, null))
        viewModel.processIntent(MovieDetailIntent.OnShareClick)

        val currentState = viewModel.state.value
        assert(currentState?.shareTrailerUrl == "https://www.youtube.com/watch?v=testKey")
    }
}