package com.icerrate.popularmovies.view.movies.search;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.icerrate.popularmovies.R;
import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.PaginatedResponse;
import com.icerrate.popularmovies.utils.ViewUtils;
import com.icerrate.popularmovies.utils.InjectionUtils;
import com.icerrate.popularmovies.view.common.BaseActivity;
import com.icerrate.popularmovies.view.common.EndlessRecyclerOnScrollListener;
import com.icerrate.popularmovies.view.movies.catalog.MoviesCatalogAdapter;
import com.icerrate.popularmovies.view.movies.detail.MovieDetailActivity;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;

import static com.icerrate.popularmovies.view.movies.detail.MovieDetailFragment.KEY_MOVIE;

/**
 * @author Ivan Cerrate.
 */

public class SearchMoviesActivity extends BaseActivity implements SearchMoviesContract.View,
        MoviesCatalogAdapter.OnItemClickListener {

    public final static String KEY_ACTIVE_SEARCH = "ACTIVE_SEARCH_KEY";

    public final static String KEY_SEARCH_QUERY = "SEARCH_QUERY_KEY";

    public final static String KEY_PAGINATED_MOVIES = "PAGINATED_MOVIES_KEY";

    @BindView(R.id.search_view)
    public SearchView searchView;

    @BindView(R.id.progress)
    public ViewStub progressBar;

    @BindView(R.id.movies)
    public RecyclerView moviesRecyclerView;

    @BindView(R.id.movies_no_data)
    public TextView noDataTextView;

    @BindView(R.id.footer_progress)
    public ViewStub footerProgressBar;

    private ImageView selectedMoviePoster;

    private MoviesCatalogAdapter adapter;

    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    private SearchMoviesPresenter presenter;

    public static Intent makeIntent(Context context){
        return new Intent(context, SearchMoviesActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movies);
        setNavigationToolbar(true);
        presenter = new SearchMoviesPresenter(this,
                InjectionUtils.movieRepository(this));
        setupView();
        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
            presenter.loadMovies();
            searchView.post(new Runnable() {
                @Override
                public void run() {
                    searchView.setQuery(presenter.getQuery(), false);
                }
            });
        }
    }

    private void setupView() {
        //Movies
        int columns = ViewUtils.getGridColumns(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, columns);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //SearchView
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.searchMovies(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void saveInstanceState(Bundle outState) {
        outState.putString(KEY_SEARCH_QUERY, presenter.getQuery());
        outState.putParcelable(KEY_PAGINATED_MOVIES, presenter.getMoviesPaginatedResponse());
    }

    @Override
    protected void restoreInstanceState(Bundle savedState) {
        String query = savedState.getString(KEY_SEARCH_QUERY);
        PaginatedResponse<Movie> moviesPaginatedResponse = savedState.getParcelable(KEY_PAGINATED_MOVIES);
        presenter.loadPresenterState(query, moviesPaginatedResponse);
    }

    @Override
    public void showProgressBar(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void showError(String errorMessage) {
        ViewUtils.createSnackbar(toolbar, errorMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void resetMovies() {
        adapter.resetItems();
        adapter.setLoading(false);
        endlessRecyclerOnScrollListener.setLoading(false);
    }

    @Override
    public void showMovies(List<Movie> movies) {
        adapter.addItems(movies);
        endlessRecyclerOnScrollListener.setLoading(false);
    }

    @Override
    public void showSearchHint(boolean show) {
        noDataTextView.setVisibility(show ? View.VISIBLE : View.GONE);
        noDataTextView.setText(getString(R.string.search_movies_hint));
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
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                selectedMoviePoster, Objects.requireNonNull(ViewCompat.getTransitionName(selectedMoviePoster)));
        startActivity(MovieDetailActivity.makeIntent(this)
                .putExtra(KEY_MOVIE, movie), options.toBundle());
        overridePendingTransition(R.anim.fade_in, R.anim.none);
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
