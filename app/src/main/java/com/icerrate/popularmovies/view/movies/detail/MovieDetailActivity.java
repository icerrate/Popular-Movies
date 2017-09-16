package com.icerrate.popularmovies.view.movies.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.icerrate.popularmovies.R;
import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.view.common.BaseActivity;
import com.icerrate.popularmovies.view.common.GlideApp;

import static com.icerrate.popularmovies.view.movies.detail.MovieDetailFragment.KEY_MOVIE;

/**
 * @author Ivan Cerrate.
 */

public class MovieDetailActivity extends BaseActivity implements MovieDetailFragmentListener {

    private ImageView backdropImageView;

    private AppBarLayout appBarLayout;

    private View titleLayout;

    private TextView titleTextView;

    private FloatingActionButton favoriteFab;

    private Integer result;

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

        backdropImageView = findViewById(R.id.backdrop);
        appBarLayout = findViewById(R.id.app_bar);
        titleLayout = findViewById(R.id.layout_title);
        titleTextView = findViewById(R.id.title);
        favoriteFab = findViewById(R.id.favorite);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (result != null) {
            setResult(result);
        }
        finish();
    }

    @Override
    public void setCollapsingTitle(final String title) {
        if (titleTextView != null) {
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
        } else {
            toolbar.setTitle(title);
        }
    }

    @Override
    public void setBackdropImage(String backdropUrl) {
        if (backdropImageView != null) {
            GlideApp.with(this)
                    .load(backdropUrl)
                    .placeholder(getResources().getDrawable(R.drawable.backdrop_placeholder))
                    .into(backdropImageView);
        }
    }

    @Override
    public void setFavoriteOnClickListener(View.OnClickListener onClickListener) {
        favoriteFab.setOnClickListener(onClickListener);
    }

    @Override
    public void updateFavoriteIcon(int icon) {
        favoriteFab.setImageResource(icon);
    }

    @Override
    public void notifyUpdate() {
        result = Activity.RESULT_OK;
    }
}
