package com.icerrate.popularmovies.view.common;

import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.icerrate.popularmovies.R;

/**
 * Created by Ivan Cerrate
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseFragmentListener {

    protected Toolbar toolbar;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void replaceFragment(int containerId, Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(containerId, fragment);
        transaction.commit();
    }

    @Override
    public void setTitle(String title) {
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }
}
