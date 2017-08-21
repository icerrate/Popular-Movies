package com.icerrate.popularmovies.view.common;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewStub;

/**
 * Created by Ivan Cerrate
 */

public abstract class BaseFragment extends Fragment implements BaseView {

    protected SwipeRefreshLayout refreshLayout;
    protected ViewStub progressBar;

    protected BaseFragmentListener fragmentListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentListener = (BaseFragmentListener) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentListener = (BaseFragmentListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }

    protected void initPresenter() {
        //Needs to be empty
    }

    protected void saveInstanceState(Bundle outState) {
        //Needs to be empty
    }

    protected void restoreInstanceState(Bundle savedState) {
        //Needs to be empty
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
    public void showRefreshLayout() {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void hideRefreshLayout() {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showError(String errorMessage) {
        //TODO: Show snackbar with error
    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
