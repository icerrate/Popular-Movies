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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.icerrate.popularmovies.R;
import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.Review;
import com.icerrate.popularmovies.data.model.Trailer;
import com.icerrate.popularmovies.utils.InjectionUtils;
import com.icerrate.popularmovies.view.common.BaseFragment;
import com.icerrate.popularmovies.view.common.BaseItemDecoration;

import java.util.ArrayList;

import static com.icerrate.popularmovies.R.id.trailers;

/**
 * @author Ivan Cerrate.
 */

public class MovieDetailFragment extends BaseFragment implements MovieDetailView,
        TrailersAdapter.OnItemClickListener, ReviewsAdapter.OnButtonClickListener {

    public static String KEY_MOVIE = "MOVIE_KEY";

    public static String KEY_TRAILERS = "TRAILERS_KEY";

    public static String KEY_REVIEWS = "REVIEWS_KEY";

    private ImageView posterImageView;

    private TextView titleDateTextView;

    private TextView releaseDateTextView;

    private TextView ratingTextView;

    private TextView synopsisTextView;

    private RecyclerView trailersRecyclerView;

    private TrailersAdapter trailersAdapter;

    private TextView trailersNoDataTextView;

    private RecyclerView reviewsRecyclerView;

    private ReviewsAdapter reviewsAdapter;

    private TextView reviewsNoDataTextView;

    private MenuItem shareMenuItem;

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
                InjectionUtils.movieRepository(getContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        posterImageView = (ImageView) view.findViewById(R.id.poster);
        titleDateTextView = (TextView) view.findViewById(R.id.title);
        releaseDateTextView = (TextView) view.findViewById(R.id.release_date);
        ratingTextView = (TextView) view.findViewById(R.id.rating);
        synopsisTextView = (TextView) view.findViewById(R.id.synopsis);
        trailersRecyclerView = (RecyclerView) view.findViewById(trailers);
        trailersNoDataTextView = (TextView) view.findViewById(R.id.trailers_no_data);
        reviewsRecyclerView = (RecyclerView) view.findViewById(R.id.reviews);
        reviewsNoDataTextView = (TextView) view.findViewById(R.id.reviews_no_data);

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
        presenter.loadView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movie_detail, menu);
        shareMenuItem = menu.getItem(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                presenter.onShareClick();
                return true;
            default:
                return false;
        }
    }
    @Override
    protected void saveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_MOVIE, presenter.getMovie());
        outState.putParcelableArrayList(KEY_TRAILERS, presenter.getTrailers());
        outState.putParcelableArrayList(KEY_REVIEWS, presenter.getReviews());
    }

    @Override
    protected void restoreInstanceState(Bundle savedState) {
        Movie movie = savedState.getParcelable(KEY_MOVIE);
        ArrayList<Trailer> trailers = savedState.getParcelableArrayList(KEY_TRAILERS);
        ArrayList<Review> reviews = savedState.getParcelableArrayList(KEY_REVIEWS);
        presenter.loadPresenterState(movie, trailers, reviews);
    }

    private void setupView() {
        //Trailers
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        trailersAdapter = new TrailersAdapter(this);
        trailersRecyclerView.setAdapter(trailersAdapter);
        trailersRecyclerView.setLayoutManager(horizontalLayoutManager);
        BaseItemDecoration mDividerItemDecoration = new BaseItemDecoration(
                getContext().getResources().getDrawable(R.drawable.decorator_white));
        trailersRecyclerView.addItemDecoration(mDividerItemDecoration);
        trailersRecyclerView.setNestedScrollingEnabled(false);
        //Reviews
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        reviewsAdapter = new ReviewsAdapter(this);
        reviewsRecyclerView.setAdapter(reviewsAdapter);
        reviewsRecyclerView.setLayoutManager(verticalLayoutManager);
        mDividerItemDecoration = new BaseItemDecoration(
                getContext().getResources().getDrawable(R.drawable.decorator_gray));
        reviewsRecyclerView.addItemDecoration(mDividerItemDecoration);
        reviewsRecyclerView.setNestedScrollingEnabled(false);
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
    public void onButtonClick(View view) {
        Review review = (Review) view.getTag();
        if (review != null) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(review.getUrl())));
        }
    }

    @Override
    public void showMovieDetail(String title, String releaseDate, String posterUrl, String backdropUrl, String rating, String synopsis) {
        movieDetailFragmentListener.setBackdropImage(backdropUrl);
        movieDetailFragmentListener.setCollapsingTitle(title);
        movieDetailFragmentListener.setFavoriteOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onFavoriteFabClicked();
            }
        });
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
    public void showTrailersNoData(boolean show) {
        trailersNoDataTextView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showReviews(ArrayList<Review> reviews) {
        reviewsAdapter.addItems(reviews);
    }

    @Override
    public void showReviewsNoData(boolean show) {
        reviewsNoDataTextView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void updateFavoriteIcon(int iconId) {
        movieDetailFragmentListener.updateFavoriteIcon(iconId);
    }

    @Override
    public void notifyUpdate() {
        movieDetailFragmentListener.notifyUpdate();
    }

    @Override
    public void prepareTrailerShare(String trailerUrl) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, trailerUrl);
        startActivity(Intent.createChooser(shareIntent, "Share Trailer link using"));
    }

    @Override
    public void showShareMenu(boolean show) {
        if (shareMenuItem != null) {
            shareMenuItem.setVisible(show);
        }
    }
}
