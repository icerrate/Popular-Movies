package com.icerrate.popularmovies.view.movies.catalog;

import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.PaginatedResponse;
import com.icerrate.popularmovies.data.source.MovieRepository;
import com.icerrate.popularmovies.mock.MockUtils;
import com.icerrate.popularmovies.view.common.BaseCallback;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MoviesCatalogPresenterTest {

    private final static boolean IS_FAVORITE = true;

    @Mock
    private PaginatedResponse<Movie> paginatedResponse;

    @Mock
    private ArrayList<Movie> regularResponse;

    @Mock
    private MoviesCatalogContract.View view;

    @Mock
    private MovieRepository movieRepository;

    @Captor
    private ArgumentCaptor<BaseCallback<PaginatedResponse<Movie>>> popularMoviesCaptor;

    @Captor
    private ArgumentCaptor<BaseCallback<PaginatedResponse<Movie>>> topRatedMoviesCaptor;

    @Captor
    private ArgumentCaptor<BaseCallback<ArrayList<Movie>>> favoriteMoviesCaptor;

    @Captor
    private ArgumentCaptor<BaseCallback<Boolean>> isFavoriteMovieCaptor;

    private MoviesCatalogPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new MoviesCatalogPresenter(view, movieRepository);
    }

    @Test
    public void loadNextMoviesPage_WithSuccess() {
        mockPaginatedResponseData(true);
        presenter.loadNextMoviesPage();
        captureMostPopularMoviesSuccess();
        verify(view).showNoDataView(false);
        verify(view).showMovies(paginatedResponse.getResults());

        verifyFinishLoading();
    }

    @Test
    public void refreshMovies_WithSuccess() {
        mockPaginatedResponseData(true);
        presenter.refreshMoviesBySortType(MoviesCatalogPresenter.SortType.MOST_POPULAR);
        verify(view).showProgressBar(true);
        captureMostPopularMoviesSuccess();
        verify(view).showNoDataView(false);
        verify(view).showMovies(paginatedResponse.getResults());

        verifyFinishLoading();
    }

    @Test
    public void loadPopularMovies_WithSuccess() {
        mockPaginatedResponseData(true);
        presenter.loadMovies();
        captureMostPopularMoviesSuccess();
        verify(view).showNoDataView(false);
        verify(view).showMovies(paginatedResponse.getResults());

        verifyFinishLoading();
    }

    @Test
    public void loadPopularMoviesBySortType_WithSuccess() {
        mockPaginatedResponseData(true);
        presenter.loadMoviesBySortType(MoviesCatalogPresenter.SortType.MOST_POPULAR);
        verify(view).showProgressBar(true);
        captureMostPopularMoviesSuccess();
        verify(view).showNoDataView(false);
        verify(view).showMovies(paginatedResponse.getResults());

        verifyFinishLoading();
    }

    @Test
    public void loadEmptyPopularMoviesBySortType_WithSuccess() {
        mockPaginatedResponseData(false);
        presenter.loadMoviesBySortType(MoviesCatalogPresenter.SortType.MOST_POPULAR);
        verify(view).showProgressBar(true);
        captureMostPopularMoviesSuccess();
        verify(view).showNoDataView(true);

        verifyFinishLoading();
    }

    @Test
    public void loadPopularMoviesBySortType_WithFailure() {
        mockPaginatedResponseData(false);
        presenter.loadMoviesBySortType(MoviesCatalogPresenter.SortType.MOST_POPULAR);
        verify(view).showProgressBar(true);
        captureMostPopularMoviesFailure();
        verify(view).showError(anyString());

        verifyFinishLoading();
    }

    @Test
    public void loadTopRatedMoviesBySortType_WithSuccess() {
        mockPaginatedResponseData(true);
        presenter.loadMoviesBySortType(MoviesCatalogPresenter.SortType.HIGHEST_RATED);
        verify(view).showProgressBar(true);
        captureTopRatedMoviesSuccess();
        verify(view).showNoDataView(false);
        verify(view).showMovies(paginatedResponse.getResults());

        verifyFinishLoading();
    }

    @Test
    public void loadEmptyTopRatedMoviesBySortType_WithSuccess() {
        mockPaginatedResponseData(false);
        presenter.loadMoviesBySortType(MoviesCatalogPresenter.SortType.HIGHEST_RATED);
        verify(view).showProgressBar(true);
        captureTopRatedMoviesSuccess();
        verify(view).showNoDataView(true);

        verifyFinishLoading();
    }

    @Test
    public void loadTopRatedMoviesBySortType_WithFailure() {
        mockPaginatedResponseData(false);
        presenter.loadMoviesBySortType(MoviesCatalogPresenter.SortType.HIGHEST_RATED);
        verify(view).showProgressBar(true);
        captureTopRatedMoviesFailure();
        verify(view).showError(anyString());

        verifyFinishLoading();
    }

    @Test
    public void loadFavoriteMoviesBySortType_WithSuccess() {
        mockRegularResponseData(true);
        presenter.loadMoviesBySortType(MoviesCatalogPresenter.SortType.FAVORITE);
        verify(view).showProgressBar(true);
        captureFavoriteMoviesSuccess();
        verify(view).showNoDataView(false);
        verify(view).showMovies(regularResponse);

        verifyFinishLoading();
    }

    @Test
    public void loadEmptyFavoriteMoviesBySortType_WithSuccess() {
        mockRegularResponseData(false);
        presenter.loadMoviesBySortType(MoviesCatalogPresenter.SortType.FAVORITE);
        verify(view).showProgressBar(true);
        captureFavoriteMoviesSuccess();
        verify(view).showNoDataView(true);

        verifyFinishLoading();
    }

    @Test
    public void loadFavoriteMoviesBySortType_WithFailure() {
        mockRegularResponseData(false);
        presenter.loadMoviesBySortType(MoviesCatalogPresenter.SortType.FAVORITE);
        verify(view).showProgressBar(true);
        captureFavoriteMoviesFailure();
        verify(view).showError(anyString());

        verifyFinishLoading();
    }

    @Test
    public void onSelectedMovieSuccess() {
        Movie movie =  MockUtils.getSingleMovie(1);
        presenter.onMovieItemClick(movie);
        captureIsFavoriteSuccess();
        movie.setFavorite(IS_FAVORITE);
        verify(view).goToMovieDetail(movie);
    }

    @Test
    public void onSelectedMovieFailure() {
        Movie movie =  MockUtils.getSingleMovie(1);
        presenter.onMovieItemClick(movie);
        captureIsFavoriteFailure();
        verify(view).showError(anyString());
    }

    @Test
    public void onBackFromDetail() {
        loadFavoriteMoviesBySortType_WithSuccess();
        presenter.onBackFromDetail();
        verify(view).showNoDataView(false);
        verify(view).showMovies(regularResponse);
        verifyFinishLoading();
    }

    @Test
    public void checkSortType() {
        MoviesCatalogPresenter.SortType sortType = MoviesCatalogPresenter.SortType.MOST_POPULAR;
        presenter.loadMoviesBySortType(sortType);
        assertEquals(sortType, presenter.getSortType());
    }

    private void verifyFinishLoading() {
        verify(view).showFooterProgress(false);
        verify(view).showProgressBar(false);
        verify(view).showRefreshLayout(false);
    }

    private void captureMostPopularMoviesSuccess() {
        verify(movieRepository).getPopularMovies(anyInt(), popularMoviesCaptor.capture());
        popularMoviesCaptor.getValue().onSuccess(paginatedResponse);
    }

    private void captureMostPopularMoviesFailure() {
        verify(movieRepository).getPopularMovies(anyInt(), popularMoviesCaptor.capture());
        popularMoviesCaptor.getValue().onFailure(anyString());
    }

    private void captureTopRatedMoviesSuccess() {
        verify(movieRepository).getTopRatedMovies(anyInt(), topRatedMoviesCaptor.capture());
        topRatedMoviesCaptor.getValue().onSuccess(paginatedResponse);
    }

    private void captureTopRatedMoviesFailure() {
        verify(movieRepository).getTopRatedMovies(anyInt(), topRatedMoviesCaptor.capture());
        topRatedMoviesCaptor.getValue().onFailure(anyString());
    }

    private void captureFavoriteMoviesSuccess() {
        verify(movieRepository).getFavoriteMovies(favoriteMoviesCaptor.capture());
        favoriteMoviesCaptor.getValue().onSuccess(regularResponse);
    }

    private void captureFavoriteMoviesFailure() {
        verify(movieRepository).getFavoriteMovies(favoriteMoviesCaptor.capture());
        favoriteMoviesCaptor.getValue().onFailure(anyString());
    }

    private void captureIsFavoriteSuccess() {
        verify(movieRepository).isFavoriteMovie(anyInt(), isFavoriteMovieCaptor.capture());
        isFavoriteMovieCaptor.getValue().onSuccess(IS_FAVORITE);
    }

    private void captureIsFavoriteFailure() {
        verify(movieRepository).isFavoriteMovie(anyInt(), isFavoriteMovieCaptor.capture());
        isFavoriteMovieCaptor.getValue().onFailure(anyString());
    }

    private void mockPaginatedResponseData(boolean hasItems) {
        when(paginatedResponse.getPage()).thenReturn(1);
        when(paginatedResponse.getTotalResults()).thenReturn(10);
        when(paginatedResponse.getTotalPages()).thenReturn(2);
        when(paginatedResponse.getResults()).thenReturn(MockUtils.getMovies(hasItems));
    }

    private void mockRegularResponseData(boolean hasItems) {
        regularResponse = MockUtils.getMovies(hasItems);
    }
}