package com.icerrate.popularmovies.view.common;

import android.support.v4.app.Fragment;

/**
 * @author Ivan Cerrate.
 */

public interface BaseFragmentListener {

    void setNavigationToolbar(boolean navigation);

    void replaceFragment(int containerId, Fragment fragment);

    void setTitle(String title);
}
