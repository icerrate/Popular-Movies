package com.icerrate.popularmovies.view.catalog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.icerrate.popularmovies.R;
import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.PaginatedResponse;
import com.icerrate.popularmovies.data.source.MovieDataSource;
import com.icerrate.popularmovies.provider.cloud.RetrofitModule;
import com.icerrate.popularmovies.view.common.BaseFragment;
import com.icerrate.popularmovies.view.common.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;

import static com.icerrate.popularmovies.view.catalog.MoviesCatalogPresenter.SortType;

/**
 * Created by Ivan Cerrate.
 */

public class MoviesCatalogFragment extends BaseFragment implements MoviesCatalogView, MoviesCatalogAdapter.OnItemClickListener {

    public static String KEY_SORT_TYPE = "SORT_TYPE_KEY";

    public static String KEY_PAGINATED_MOVIES = "PAGINATED_MOVIES_KEY";

    private RecyclerView moviesRecyclerView;

    private MoviesCatalogPresenter presenter;

    private MoviesCatalogAdapter adapter;

    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    protected ViewStub footerProgressBar;

    public static MoviesCatalogFragment newInstance() {
        return new MoviesCatalogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MoviesCatalogPresenter(this,
                new MovieDataSource(RetrofitModule.get().provideMovieAPI()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_movies_catalog, container, false);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        progressBar = (ViewStub) view.findViewById(R.id.progress);
        moviesRecyclerView = (RecyclerView) view.findViewById(R.id.movies);
        footerProgressBar = (ViewStub) view.findViewById(R.id.footer_progress);

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
            presenter.setSortType(SortType.MOST_POPULAR);
            presenter.refreshMovies();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movies_catalog, menu);
        SortType currentSortType = presenter.getSortType();
        if (currentSortType.equals(SortType.MOST_POPULAR)) {
            MenuItem item = menu.findItem(R.id.popular);
            item.setChecked(true);
        } else if (currentSortType.equals(SortType.HIGHEST_RATED)) {
            MenuItem item = menu.findItem(R.id.top_rated);
            item.setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular:
                presenter.setSortType(SortType.MOST_POPULAR);
                presenter.refreshMovies();
                break;
            case R.id.top_rated:
                presenter.setSortType(SortType.HIGHEST_RATED);
                presenter.refreshMovies();
                break;
            default:
                return false;
        }
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
        SortType sortType = (SortType) savedState.getSerializable(KEY_SORT_TYPE);
        PaginatedResponse<Movie> moviesPaginatedResponse = savedState.getParcelable(KEY_PAGINATED_MOVIES);
        presenter.loadPresenterState(sortType, moviesPaginatedResponse);
    }

    private void setupView() {
        //Movies
        int columns = getColumns();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), columns);
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore() {
                footerProgressBar.setVisibility(View.VISIBLE);
                adapter.setLoading(true);
                presenter.loadNextMoviesPage();
            }
        };
        adapter = new MoviesCatalogAdapter(this, columns);
        moviesRecyclerView.setAdapter(adapter);
        moviesRecyclerView.setLayoutManager(gridLayoutManager);
        moviesRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onSwipeMovies();
            }
        });
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
    }

    private int getColumns() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        if (display.getRotation() == Surface.ROTATION_0) {
            return 2;
        } else {
            return 4;
        }
    }

    @Override
    public void onItemClick(View view) {
        Movie movie = (Movie) view.getTag(view.getId());
        /*if (movie != null) {
            startActivity(MovieDetail.makeIntent(getContext())
                    .putExtra(MOVIE_ID_KEY, movie.getId()));
        }*/
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
        adapter.setLoading(false);
        footerProgressBar.setVisibility(View.GONE);
    }
}
