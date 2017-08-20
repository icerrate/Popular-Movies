package com.icerrate.popularmovies.view.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;

import com.icerrate.popularmovies.R;
import com.icerrate.popularmovies.utils.DialogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ivan Cerrate
 */
public class BaseActivity extends AppCompatActivity implements BaseView {

    @Bind(R.id.toolbar) protected Toolbar toolbar;
    @Bind(R.id.refresh) protected SwipeRefreshLayout refreshLayout;
    @Bind(R.id.progress) protected ViewStub progressBar;

    private ProgressDialog progressDialog;
    private AlertDialog errorAlertDialog;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        initPresenter();
    }

    @Override
    public void setToolbar(@Nullable String title) {
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.title_activity_movies));
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public void showProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideRefreshLayout() {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showProgressDialog() {
        showProgressDialog(getString(R.string.message_loading));
    }

    @Override
    public void showProgressDialog(String message) {
        if (progressDialog != null) {
            progressDialog.setMessage(message);
        } else {
            progressDialog = DialogUtils.createProgressDialog(this, null);
        }
        progressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showError(String errorMessage) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (errorAlertDialog != null) {
            errorAlertDialog.setMessage(errorMessage);
        } else {
            errorAlertDialog = DialogUtils.createErrorDialog(this, errorMessage);
        }
        errorAlertDialog.show();
    }

    @Override
    public Context getContext() {
        return null;
    }

    protected void initPresenter() {
        //Needs to be empty
    }
}
