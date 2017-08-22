package com.icerrate.popularmovies.view.movies.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.icerrate.popularmovies.R;
import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.source.MovieDataSource;
import com.icerrate.popularmovies.provider.cloud.RetrofitModule;
import com.icerrate.popularmovies.view.common.BaseFragment;

/**
 * Created by Ivan Cerrate.
 */

public class MovieDetailFragment extends BaseFragment implements MovieDetailView {

    public static String KEY_MOVIE = "MOVIE_KEY";

    private TextView titleTextView;

    private ImageView posterImageView;

    private TextView releaseDateTextView;

    private TextView ratingTextView;

    private TextView synopsisTextView;

    private MovieDetailPresenter presenter;

    public static MovieDetailFragment newInstance(Movie movie) {
        Bundle bundle = new Bundle();
        if (movie != null) {
            bundle.putParcelable(KEY_MOVIE, movie);
        }
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MovieDetailPresenter(this,
                new MovieDataSource(RetrofitModule.get().provideMovieAPI()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        titleTextView = (TextView) view.findViewById(R.id.title);
        posterImageView = (ImageView) view.findViewById(R.id.poster);
        releaseDateTextView = (TextView) view.findViewById(R.id.release_date);
        ratingTextView = (TextView) view.findViewById(R.id.rating);
        synopsisTextView = (TextView) view.findViewById(R.id.synopsis);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupView();
        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        } else {
            Movie movie = getArguments().getParcelable(KEY_MOVIE);
            presenter.setMovieDetail(movie);
        }
        presenter.loadMovie();
    }

    @Override
    protected void saveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_MOVIE, presenter.getMovie());
    }

    @Override
    protected void restoreInstanceState(Bundle savedState) {
        Movie movie = savedState.getParcelable(KEY_MOVIE);
        presenter.loadPresenterState(movie);
    }

    private void setupView() {

    }

    @Override
    public void showMovieDetail(String title, String releaseDate, String posterUrl, String rating, String synopsis) {
        titleTextView.setText(title);
        Glide.with(this)
                .load(posterUrl)
                .placeholder(getResources().getDrawable(R.drawable.movie_placeholder))
                .into(posterImageView);
        releaseDateTextView.setText(releaseDate);
        ratingTextView.setText(rating);
        synopsisTextView.setText(synopsis);
    }
}
