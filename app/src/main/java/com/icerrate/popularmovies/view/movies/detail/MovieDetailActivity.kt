package com.icerrate.popularmovies.view.movies.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.material.appbar.AppBarLayout
import com.icerrate.popularmovies.databinding.ActivityMovieDetailBinding
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.icerrate.popularmovies.R
import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.view.common.BaseActivity
import com.icerrate.popularmovies.view.movies.detail.MovieDetailFragment.Companion.KEY_MOVIE

/**
 * @author Ivan Cerrate.
 */
@AndroidEntryPoint
class MovieDetailActivity : BaseActivity<ActivityMovieDetailBinding>(
    ActivityMovieDetailBinding::inflate
), MovieDetailFragmentListener {

    private var result: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding.favorite.visibility = View.INVISIBLE
                result?.let { setResult(it) }
                finish()
            }
        })
        setTitle("")
        setNavigationToolbar(true)
        if (savedInstanceState == null) {
            val movie = intent.getParcelableExtra<Movie>(KEY_MOVIE)
            val movieDetailFragment = MovieDetailFragment.newInstance(movie!!)
            replaceFragment(R.id.content, movieDetailFragment)
        }
    }

    override fun setCollapsingTitle(title: String) {
        binding.title?.let { titleTextView ->
            titleTextView.text = title
            binding.layoutTitle?.post {
                val layoutParams = binding.toolbar.layoutParams as CollapsingToolbarLayout.LayoutParams
                layoutParams.height = binding.layoutTitle!!.height
                binding.toolbar.layoutParams = layoutParams
            }

            binding.appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
                private var isShow = false
                private var animatedIn = false
                private var animatedOut = false
                private var scrollRange = -1
                private val fadeIn = AlphaAnimation(0.0f, 1.0f)
                private val fadeOut = AlphaAnimation(1.0f, 0.0f)

                override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.totalScrollRange
                    }
                    if (scrollRange + verticalOffset < 100) {
                        if (!animatedIn) {
                            fadeIn.duration = 350
                            fadeIn.fillAfter = true
                            titleTextView.startAnimation(fadeIn)
                            animatedIn = true
                            animatedOut = false
                        }
                        isShow = true
                    } else if (isShow) {
                        if (!animatedOut) {
                            fadeOut.duration = 350
                            fadeOut.fillAfter = true
                            titleTextView.startAnimation(fadeOut)
                            animatedOut = true
                            animatedIn = false
                        }
                        isShow = false
                    }
                }
            })
        } ?: run {
            binding.toolbarTitle?.text = title
        }
    }

    override fun setBackdropImage(backdropUrl: String) {
        binding.backdrop?.let {
            Glide.with(this)
                .load(backdropUrl)
                .placeholder(androidx.core.content.ContextCompat.getDrawable(this, R.drawable.backdrop_placeholder))
                .into(it)
        }
    }

    override fun setFavoriteOnClickListener(onClickListener: View.OnClickListener) {
        binding.favorite.setOnClickListener(onClickListener)
    }

    override fun setFavoriteState(isFavorite: Boolean) {
        val animation = AnimationUtils.loadAnimation(this, R.anim.bounce_anim)
        animation.startOffset = FAVORITE_ICON_OFFSET
        binding.favorite.startAnimation(animation)
        binding.favorite.isSelected = isFavorite
    }

    override fun updateFavoriteState(isFavorite: Boolean) {
        binding.favorite.isSelected = isFavorite
        result = Activity.RESULT_OK
    }

    companion object {
        private const val FAVORITE_ICON_OFFSET = 200L

        fun makeIntent(context: Context): Intent = Intent(context, MovieDetailActivity::class.java)
    }
}