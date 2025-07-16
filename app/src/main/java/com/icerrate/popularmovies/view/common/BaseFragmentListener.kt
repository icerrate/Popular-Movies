package com.icerrate.popularmovies.view.common

import androidx.fragment.app.Fragment

/**
 * @author Ivan Cerrate.
 */
interface BaseFragmentListener {

    fun setNavigationToolbar(navigation: Boolean)

    fun replaceFragment(containerId: Int, fragment: Fragment)
}