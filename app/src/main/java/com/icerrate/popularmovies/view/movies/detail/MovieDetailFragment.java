package com.icerrate.popularmovies.view.movies.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.icerrate.popularmovies.R;
import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.Trailer;
import com.icerrate.popularmovies.data.source.MovieDataSource;
import com.icerrate.popularmovies.provider.cloud.RetrofitModule;
import com.icerrate.popularmovies.view.common.BaseFragment;

import java.util.ArrayList;

/**
 * Created by Ivan Cerrate.
 */

public class MovieDetailFragment extends BaseFragment implements MovieDetailView, TrailersAdapter.OnItemClickListener {

    public static String KEY_MOVIE = "MOVIE_KEY";

    private ImageView posterImageView;

    private TextView titleDateTextView;

    private TextView releaseDateTextView;

    private TextView ratingTextView;

    private TextView synopsisTextView;

    private RecyclerView trailersRecyclerView;

    private TrailersAdapter trailersAdapter;

    private MovieDetailPresenter presenter;

    private MovieDetailFragmentListener movieDetailFragmentListener;

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        castOrThrowException(activity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        castOrThrowException(context);
    }

    private void castOrThrowException(Context context) {
        try {
            movieDetailFragmentListener = (MovieDetailFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement MovieDetailFragmentListener");
        }
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

        posterImageView = (ImageView) view.findViewById(R.id.poster);
        titleDateTextView = (TextView) view.findViewById(R.id.title);
        releaseDateTextView = (TextView) view.findViewById(R.id.release_date);
        ratingTextView = (TextView) view.findViewById(R.id.rating);
        synopsisTextView = (TextView) view.findViewById(R.id.synopsis);
        trailersRecyclerView = (RecyclerView) view.findViewById(R.id.trailers);

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
        //Trailers
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        trailersAdapter = new TrailersAdapter(this);
        trailersRecyclerView.setAdapter(trailersAdapter);
        trailersRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onItemClick(View view) {
        Trailer trailer = (Trailer) view.getTag();
        if (trailer != null) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(trailer.getVideoUrl())));
        }
    }

    @Override
    public void showMovieDetail(String title, String releaseDate, String posterUrl, String backdropUrl, String rating, String synopsis) {
        movieDetailFragmentListener.setBackdropImage(backdropUrl);
        movieDetailFragmentListener.setCollapsingTitle(title);
        titleDateTextView.setText(title);
        Glide.with(this)
                .load(posterUrl)
                .placeholder(getResources().getDrawable(R.drawable.poster_placeholder))
                .into(posterImageView);
        releaseDateTextView.setText(releaseDate);
        ratingTextView.setText(rating);
        synopsisTextView.setText(synopsis);
    }

    @Override
    public void showTrailers(ArrayList<Trailer> trailers) {
        trailersAdapter.addItems(trailers);
    }

    @Override
    public void resetTrailers() {
        trailersAdapter.resetItems();
    }
}
