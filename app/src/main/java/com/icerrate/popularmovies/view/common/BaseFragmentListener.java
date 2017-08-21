package com.icerrate.popularmovies.view.common;

import android.support.v4.app.Fragment;

/**
 * Created by Ivan Cerrate.
 */

public interface BaseFragmentListener {

    void replaceFragment(int containerId, Fragment fragment);
    void setTitle(String title);
}
