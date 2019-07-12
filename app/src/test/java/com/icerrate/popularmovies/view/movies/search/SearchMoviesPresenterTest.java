package com.icerrate.popularmovies.view.movies.search;

import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.PaginatedResponse;
import com.icerrate.popularmovies.data.source.MovieRepository;
import com.icerrate.popularmovies.mock.MockUtils;
import com.icerrate.popularmovies.view.common.BaseCallback;
import com.icerrate.popularmovies.view.movies.catalog.MoviesCatalogContract;
import com.icerrate.popularmovies.view.movies.catalog.MoviesCatalogPresenter;

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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SearchMoviesPresenterTest {

    private final static String VALID_SEARCH_QUERY = "query";

    private final static String INVALID_SEARCH_QUERY = "qu";

    private final static boolean IS_FAVORITE = true;

    @Mock
    private PaginatedResponse<Movie> paginatedResponse;

    @Mock
    private SearchMoviesContract.View view;

    @Mock
    private MovieRepository movieRepository;

    @Captor
    private ArgumentCaptor<BaseCallback<PaginatedResponse<Movie>>> searchMoviesCaptor;

    @Captor
    private ArgumentCaptor<BaseCallback<Boolean>> isFavoriteMovieCaptor;

    private SearchMoviesPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new SearchMoviesPresenter(view, movieRepository);
    }

    @Test
    public void searchMoviesValidQuery_WithSuccess() {
        mockPaginatedResponseData(true);
        presenter.searchMovies(VALID_SEARCH_QUERY);
        verify(view).showProgressBar(true);
        captureSearchMoviesSuccess();
        verify(view).showNoDataView(false);
        verify(view).showMovies(paginatedResponse.getResults());

        verifyFinishLoading();
    }

    @Test
    public void searchMoviesInvalidQuery() {
        mockPaginatedResponseData(true);
        presenter.searchMovies(INVALID_SEARCH_QUERY);
        verify(view).resetMovies();
        verify(view).showSearchHint(true);
    }

    @Test
    public void searchEmptyMoviesValidQuery_WithSuccess() {
        mockPaginatedResponseData(false);
        presenter.searchMovies(VALID_SEARCH_QUERY);
        verify(view).showProgressBar(true);
        captureSearchMoviesSuccess();
        verify(view).showNoDataView(true);

        verifyFinishLoading();
    }

    @Test
    public void searchMoviesValidQuery_WithFailure() {
        mockPaginatedResponseData(false);
        presenter.searchMovies(VALID_SEARCH_QUERY);
        verify(view).showProgressBar(true);
        captureSearchMoviesFailure();
        verify(view).showError(anyString());
        verify(view).showNoDataView(true);

        verifyFinishLoading();
    }

    @Test
    public void loadNextMoviesPage_WithSuccess() {
        mockPaginatedResponseData(true);
        presenter.loadNextMoviesPage();
        captureSearchMoviesSuccess();
        verify(view).showNoDataView(false);
        verify(view).showMovies(paginatedResponse.getResults());

        verifyFinishLoading();
    }

    @Test
    public void loadMovies() {
        mockPaginatedResponseData(true);
        searchMoviesValidQuery_WithSuccess();

        presenter.loadMovies();
        verify(view, times(2)).resetMovies();
        verify(view, times(2)).showMovies(paginatedResponse.getResults());
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
    public void checkSortType() {
        presenter.searchMovies(VALID_SEARCH_QUERY);
        assertEquals(VALID_SEARCH_QUERY, presenter.getQuery());
    }

    private void verifyFinishLoading() {
        verify(view).showFooterProgress(false);
        verify(view).showProgressBar(false);
    }

    private void captureSearchMoviesSuccess() {
        verify(movieRepository).searchMovies(anyString(), anyInt(), searchMoviesCaptor.capture());
        searchMoviesCaptor.getValue().onSuccess(paginatedResponse);
    }

    private void captureSearchMoviesFailure() {
        verify(movieRepository).searchMovies(anyString(), anyInt(), searchMoviesCaptor.capture());
        searchMoviesCaptor.getValue().onFailure(anyString());
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
}