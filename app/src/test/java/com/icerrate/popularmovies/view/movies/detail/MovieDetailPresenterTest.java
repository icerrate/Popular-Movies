package com.icerrate.popularmovies.view.movies.detail;

import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.PaginatedResponse;
import com.icerrate.popularmovies.data.model.Review;
import com.icerrate.popularmovies.data.model.Trailer;
import com.icerrate.popularmovies.data.model.TrailersResponse;
import com.icerrate.popularmovies.data.source.MovieRepository;
import com.icerrate.popularmovies.mock.MockUtils;
import com.icerrate.popularmovies.view.common.BaseCallback;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MovieDetailPresenterTest {

    private final static String POSTER_CODE = "w342";

    private final static String BACKDROP_CODE = "w780";

    private final static String ERROR_MESSAGE = "error_message";

    @Mock
    private TrailersResponse<Trailer> trailersResponse;

    @Mock
    private PaginatedResponse<Review> paginatedReviewsResponse;

    @Mock
    private MovieDetailContract.View view;

    @Mock
    private MovieRepository movieRepository;

    @Captor
    private ArgumentCaptor<BaseCallback<TrailersResponse<Trailer>>> trailersCaptor;

    @Captor
    private ArgumentCaptor<BaseCallback<PaginatedResponse<Review>>> reviewsCaptor;

    @Captor
    private ArgumentCaptor<BaseCallback<Void>> removeFavoriteCaptor;

    @Captor
    private ArgumentCaptor<BaseCallback<Void>> addFavoriteCaptor;

    private MovieDetailPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new MovieDetailPresenter(view, movieRepository);
    }

    @Test
    public void checkMovieDetail() {
        Movie movieDetail = MockUtils.getSingleMovie(1);
        presenter.setMovieDetail(movieDetail);

        assertEquals(movieDetail, presenter.getMovie());
    }

    @Test
    public void loadMovieDetailsHeader() {
        Movie movieDetail = MockUtils.getSingleMovie(1);
        presenter.setMovieDetail(movieDetail);

        presenter.loadMovieDetails();
        verify(view).showHeader(movieDetail.getTitle(),
                null, movieDetail.getPosterUrl(POSTER_CODE),
                movieDetail.getBackdropUrl(BACKDROP_CODE),
                String.valueOf(movieDetail.getVoteAverage()),
                movieDetail.getOverview());
        verify(view).showFavoriteState(movieDetail.isFavorite());
    }

    @Test
    public void loadMovieTrailers_WithSuccess() {
        prepareMovieDetailsFlow(true);

        presenter.loadMovieDetails();
        captureTrailersSuccess();
        verify(view).showTrailersNoData(false);
        verify(view).showTrailers(trailersResponse.getResults());
        verify(view).showShareMenu(true);
    }

    @Test
    public void loadEmptyMovieTrailers_WithSuccess() {
        prepareMovieDetailsFlow(false);

        presenter.loadMovieDetails();
        captureTrailersSuccess();
        verify(view).showShareMenu(false);
        verify(view).showTrailersNoData(true);
    }

    @Test
    public void loadMovieTrailers_WithFailure() {
        Movie movieDetail = MockUtils.getSingleMovie(1);
        presenter.setMovieDetail(movieDetail);

        presenter.loadMovieDetails();
        captureTrailersFailure();
        verify(view).showError(anyString());
        verify(view).showShareMenu(false);
        verify(view).showTrailersNoData(true);
    }

    @Test
    public void loadMovieReviews_WithSuccess() {
        prepareMovieDetailsFlow(true);

        presenter.loadMovieDetails();
        captureReviewsSuccess();
        verify(view).showReviewsNoData(false);
        verify(view).showReviews(paginatedReviewsResponse.getResults());
    }

    @Test
    public void loadEmptyMovieReviews_WithSuccess() {
        prepareMovieDetailsFlow(false);

        presenter.loadMovieDetails();
        captureReviewsSuccess();
        verify(view).showReviewsNoData(true);
    }

    @Test
    public void loadMovieReviews_WithFailure() {
        Movie movieDetail = MockUtils.getSingleMovie(1);
        presenter.setMovieDetail(movieDetail);

        presenter.loadMovieDetails();
        captureReviewsFailure();
        verify(view).showError(anyString());
        verify(view).showReviewsNoData(true);
    }

    @Test
    public void onFavoriteFabClicked_RemoveFavoriteSuccess() {
        Movie movieDetail = MockUtils.getSingleMovie(1);
        movieDetail.setFavorite(true);
        presenter.setMovieDetail(movieDetail);

        presenter.onFavoriteFabClicked();
        captureRemoveFavoriteSuccess();
        verify(view).updateFavoriteState(false);
        verify(view).showSnackbarMessage(anyInt());
    }

    @Test
    public void onFavoriteFabClicked_RemoveFavoriteFailure() {
        Movie movieDetail = MockUtils.getSingleMovie(1);
        movieDetail.setFavorite(true);
        presenter.setMovieDetail(movieDetail);

        presenter.onFavoriteFabClicked();
        captureRemoveFavoriteFailure();
        verify(view).showError(anyString());
    }

    @Test
    public void onFavoriteFabClicked_AddFavoriteSuccess() {
        Movie movieDetail = MockUtils.getSingleMovie(1);
        movieDetail.setFavorite(false);
        presenter.setMovieDetail(movieDetail);

        presenter.onFavoriteFabClicked();
        captureAddFavoriteSuccess();
        verify(view).updateFavoriteState(true);
        verify(view).showSnackbarMessage(anyInt());
    }

    @Test
    public void onFavoriteFabClicked_AddFavoriteFailure() {
        Movie movieDetail = MockUtils.getSingleMovie(1);
        movieDetail.setFavorite(false);
        presenter.setMovieDetail(movieDetail);

        presenter.onFavoriteFabClicked();
        captureAddFavoriteFailure();
        verify(view).showError(anyString());
    }

    @Test
    public void onShareClick() {
        loadMovieTrailers_WithSuccess();
        presenter.onShareClick();
        verify(view).prepareTrailerShare(trailersResponse.getResults().get(0).getVideoUrl());
    }

    @Test
    public void validateMenu() {
        loadMovieTrailers_WithSuccess();
        presenter.validateMenu();
        verify(view, times(2)).showShareMenu(true);
    }

    @Test
    public void checkTrailers() {
        loadMovieTrailers_WithSuccess();

        assertEquals(trailersResponse.getResults(), presenter.getTrailers());
    }

    @Test
    public void checkReviews() {
        loadMovieReviews_WithSuccess();

        assertEquals(paginatedReviewsResponse.getResults(), presenter.getReviews());
    }

    private void prepareMovieDetailsFlow(boolean hasItems) {
        mockTrailersResponseData(hasItems);
        mockPaginatedReviewsResponseData(hasItems);
        Movie movieDetail = MockUtils.getSingleMovie(1);
        presenter.setMovieDetail(movieDetail);
    }

    private void captureTrailersSuccess() {
        verify(movieRepository).getMovieTrailers(anyInt(), trailersCaptor.capture());
        trailersCaptor.getValue().onSuccess(trailersResponse);
    }

    private void captureTrailersFailure() {
        verify(movieRepository).getMovieTrailers(anyInt(), trailersCaptor.capture());
        trailersCaptor.getValue().onFailure(ERROR_MESSAGE);
    }

    private void captureReviewsSuccess() {
        verify(movieRepository).getMovieReviews(anyInt(), reviewsCaptor.capture());
        reviewsCaptor.getValue().onSuccess(paginatedReviewsResponse);
    }

    private void captureReviewsFailure() {
        verify(movieRepository).getMovieReviews(anyInt(), reviewsCaptor.capture());
        reviewsCaptor.getValue().onFailure(ERROR_MESSAGE);
    }

    private void captureRemoveFavoriteSuccess() {
        verify(movieRepository).removeFavoriteMovie(anyInt(), removeFavoriteCaptor.capture());
        removeFavoriteCaptor.getValue().onSuccess(null);
    }

    private void captureRemoveFavoriteFailure() {
        verify(movieRepository).removeFavoriteMovie(anyInt(), removeFavoriteCaptor.capture());
        removeFavoriteCaptor.getValue().onFailure(ERROR_MESSAGE);
    }

    private void captureAddFavoriteSuccess() {
        verify(movieRepository).addFavoriteMovie(any(Movie.class), addFavoriteCaptor.capture());
        addFavoriteCaptor.getValue().onSuccess(null);
    }

    private void captureAddFavoriteFailure() {
        verify(movieRepository).addFavoriteMovie(any(Movie.class), addFavoriteCaptor.capture());
        addFavoriteCaptor.getValue().onFailure(ERROR_MESSAGE);
    }

    private void mockTrailersResponseData(boolean hasItems) {
        when(trailersResponse.getPage()).thenReturn(1);
        when(trailersResponse.getResults()).thenReturn(MockUtils.getTrailers(hasItems));
    }

    private void mockPaginatedReviewsResponseData(boolean hasItems) {
        when(paginatedReviewsResponse.getPage()).thenReturn(1);
        when(paginatedReviewsResponse.getTotalResults()).thenReturn(10);
        when(paginatedReviewsResponse.getTotalPages()).thenReturn(2);
        when(paginatedReviewsResponse.getResults()).thenReturn(MockUtils.getReviews(hasItems));
    }
}
