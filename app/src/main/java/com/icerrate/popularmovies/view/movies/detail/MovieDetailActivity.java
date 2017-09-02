package com.icerrate.popularmovies.view.movies.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.icerrate.popularmovies.R;
import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.view.common.BaseActivity;

import static com.icerrate.popularmovies.view.movies.detail.MovieDetailFragment.KEY_MOVIE;

/**
 * Created by Ivan Cerrate
 */

public class MovieDetailActivity extends BaseActivity implements MovieDetailFragmentListener {

    private ImageView backdropImageView;

    private AppBarLayout appBarLayout;

    private View titleLayout;

    private TextView titleTextView;

    public static Intent makeIntent(Context context){
        return new Intent(context, MovieDetailActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setTitle("");
        setNavigationToolbar(true);
        if (savedInstanceState == null) {
            Movie movie = getIntent().getParcelableExtra(KEY_MOVIE);
            MovieDetailFragment movieDetailFragment = MovieDetailFragment.newInstance(movie);
            replaceFragment(R.id.content, movieDetailFragment);
        }

        backdropImageView = (ImageView) findViewById(R.id.backdrop);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        titleLayout = findViewById(R.id.layout_title);
        titleTextView = (TextView) findViewById(R.id.title);
    }

    @Override
    public void setCollapsingTitle(final String title) {
        titleTextView.setText(title);
        titleLayout.post(new Runnable() {
            @Override
            public void run() {
                CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
                layoutParams.height = titleLayout.getHeight();
                toolbar.setLayoutParams(layoutParams);
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            boolean animatedIn = false;
            boolean animatedOut = false;
            int scrollRange = -1;
            AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;
            AlphaAnimation fadeOut = new AlphaAnimation( 1.0f , 0.0f ) ;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset < 100) {
                    if (!animatedIn) {
                        fadeIn.setDuration(350);
                        fadeIn.setFillAfter(true);
                        titleTextView.startAnimation(fadeIn);
                        animatedIn = true;
                        animatedOut = false;
                    }
                    isShow = true;
                } else if (isShow) {
                    if (!animatedOut) {
                        fadeOut.setDuration(350);
                        fadeOut.setFillAfter(true);
                        titleTextView.startAnimation(fadeOut);
                        animatedOut = true;
                        animatedIn = false;
                    }
                    isShow = false;
                }
            }
        });
    }

    @Override
    public void setBackdropImage(String backdropUrl) {
        Glide.with(this)
                .load(backdropUrl)
                .placeholder(getResources().getDrawable(R.drawable.poster_placeholder))
                .into(backdropImageView);
    }
}
