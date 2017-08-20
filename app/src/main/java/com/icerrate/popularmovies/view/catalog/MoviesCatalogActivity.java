package com.icerrate.popularmovies.view.catalog;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.icerrate.popularmovies.R;
import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.source.MovieDataSource;
import com.icerrate.popularmovies.provider.cloud.RetrofitModule;
import com.icerrate.popularmovies.view.common.BaseActivity;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by Ivan Cerrate
 */

public class MoviesCatalogActivity extends BaseActivity implements MoviesCatalogView, MoviesCatalogAdapter.OnItemClickListener {

    @Bind(R.id.movies) protected RecyclerView moviesRecyclerView;

    private MoviesCatalogPresenter presenter;
    private MoviesCatalogAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_catalog);
        super.onStart();setupView();
        presenter.loadMovies();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movies_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular:
                presenter.loadMovies(MoviesCatalogPresenter.SortType.POPULARITY);
                break;
            case R.id.top_rated:
                presenter.loadMovies(MoviesCatalogPresenter.SortType.TOP_RATED);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    protected void initPresenter() {
        presenter = new MoviesCatalogPresenter(this,
                new MovieDataSource(RetrofitModule.get().provideMovieAPI()));
    }

    private void setupView() {
        //Toolbar
        setToolbar(getString(R.string.title_activity_movies));

        //Movies
        moviesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new MoviesCatalogAdapter(this);
        moviesRecyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshMovies(presenter.getSortType());
            }
        });
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
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
    }

    @Override
    public void showMovies(ArrayList<Movie> movies) {
        adapter.addItems(movies);
    }
}
