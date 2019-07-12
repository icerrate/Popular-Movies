package com.icerrate.popularmovies.view.movies.catalog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.icerrate.popularmovies.R;
import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.PaginatedResponse;
import com.icerrate.popularmovies.utils.InjectionUtils;
import com.icerrate.popularmovies.utils.ViewUtils;
import com.icerrate.popularmovies.view.common.BaseFragment;
import com.icerrate.popularmovies.view.common.EndlessRecyclerOnScrollListener;
import com.icerrate.popularmovies.view.movies.detail.MovieDetailActivity;
import com.icerrate.popularmovies.view.movies.catalog.MoviesCatalogPresenter.SortType;
import com.icerrate.popularmovies.view.movies.search.SearchMoviesActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.icerrate.popularmovies.view.movies.detail.MovieDetailFragment.KEY_MOVIE;

/**
 * @author Ivan Cerrate.
 */

public class MoviesCatalogFragment extends BaseFragment implements MoviesCatalogContract.View,
        MoviesCatalogAdapter.OnItemClickListener {

    public final static int RC_MOVIE_DETAIL = 25;

    public final static String KEY_SORT_TYPE = "SORT_TYPE_KEY";

    public final static String KEY_PAGINATED_MOVIES = "PAGINATED_MOVIES_KEY";

    @BindView(R.id.movies)
    public RecyclerView moviesRecyclerView;

    @BindView(R.id.movies_no_data)
    public TextView noDataTextView;

    @BindView(R.id.footer_progress)
    public ViewStub footerProgressBar;

    private MoviesCatalogAdapter adapter;

    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    private MoviesCatalogPresenter presenter;

    private ImageView selectedMoviePoster;

    public static MoviesCatalogFragment newInstance() {
        return new MoviesCatalogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MoviesCatalogPresenter(this,
                InjectionUtils.movieRepository(getContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_movies_catalog, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupView();
        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
            presenter.loadMovies();
        } else {
            presenter.loadMoviesBySortType(SortType.MOST_POPULAR);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movies_catalog, menu);
        MoviesCatalogPresenter.SortType currentSortType = presenter.getSortType();
        if (currentSortType.equals(MoviesCatalogPresenter.SortType.MOST_POPULAR)) {
            MenuItem item = menu.findItem(R.id.popular);
            item.setChecked(true);
        } else if (currentSortType.equals(MoviesCatalogPresenter.SortType.HIGHEST_RATED)) {
            MenuItem item = menu.findItem(R.id.top_rated);
            item.setChecked(true);
        } else if (currentSortType.equals(MoviesCatalogPresenter.SortType.FAVORITE)) {
            MenuItem item = menu.findItem(R.id.favorite);
            item.setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SortType selectedSortType;
        switch (item.getItemId()) {
            case R.id.popular:
                selectedSortType = SortType.MOST_POPULAR;
                break;
            case R.id.top_rated:
                selectedSortType = SortType.HIGHEST_RATED;
                break;
            case R.id.favorite:
                selectedSortType = SortType.FAVORITE;
                break;
            case R.id.action_search:
                startActivity(SearchMoviesActivity.makeIntent(getContext()));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.none);
                return true;
            default:
                return false;
        }
        presenter.refreshMoviesBySortType(selectedSortType);
        item.setChecked(true);
        return true;
    }

    @Override
    protected void saveInstanceState(Bundle outState) {
        outState.putSerializable(KEY_SORT_TYPE, presenter.getSortType());
        outState.putParcelable(KEY_PAGINATED_MOVIES, presenter.getMoviesPaginatedResponse());
    }

    @Override
    protected void restoreInstanceState(Bundle savedState) {
        MoviesCatalogPresenter.SortType sortType = (MoviesCatalogPresenter.SortType) savedState.getSerializable(KEY_SORT_TYPE);
        PaginatedResponse<Movie> moviesPaginatedResponse = savedState.getParcelable(KEY_PAGINATED_MOVIES);
        presenter.loadPresenterState(sortType, moviesPaginatedResponse);
    }

    private void setupView() {
        //Movies
        int columns = ViewUtils.getGridColumns(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), columns);
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore() {
                presenter.loadNextMoviesPage();
            }
        };
        adapter = new MoviesCatalogAdapter(this, columns);
        moviesRecyclerView.setAdapter(adapter);
        moviesRecyclerView.setLayoutManager(gridLayoutManager);
        moviesRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);
        //refresh
        if (refreshLayout != null) {
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    presenter.refreshMovies();
                }
            });
            refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_MOVIE_DETAIL && resultCode == Activity.RESULT_OK) {
            presenter.onBackFromDetail();
        }
    }

    @Override
    public void resetMovies() {
        adapter.resetItems();
        adapter.setLoading(false);
        endlessRecyclerOnScrollListener.setLoading(false);
    }

    @Override
    public void showMovies(ArrayList<Movie> movies) {
        adapter.addItems(movies);
        endlessRecyclerOnScrollListener.setLoading(false);
    }

    @Override
    public void showNoDataView(boolean show) {
        noDataTextView.setVisibility(show ? View.VISIBLE : View.GONE);
        noDataTextView.setText(getString(R.string.movies_no_data));
    }

    @Override
    public void showFooterProgress(boolean show) {
        adapter.setLoading(show);
        footerProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void goToMovieDetail(Movie movie) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                selectedMoviePoster, getString(R.string.movie_poster_transition));
        startActivityForResult(MovieDetailActivity.makeIntent(getActivity())
                .putExtra(KEY_MOVIE, movie), RC_MOVIE_DETAIL, options.toBundle());
        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.none);
    }

    @Override
    public void onItemClick(View view) {
        Movie movie = (Movie) view.getTag();
        if (movie != null) {
            selectedMoviePoster = view.findViewById(R.id.poster);
            presenter.onMovieItemClick(movie);
        }
    }
}
