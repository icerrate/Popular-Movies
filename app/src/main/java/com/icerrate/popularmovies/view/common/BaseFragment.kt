package com.icerrate.popularmovies.view.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.icerrate.popularmovies.utils.ViewUtils

/**
 * @author Ivan Cerrate.
 */

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<V: ViewBinding>(
    private val inflate: Inflate<V>
) : Fragment() {

    protected lateinit var binding: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate(inflater, container, false)
        return binding.root
    }
    override fun onSaveInstanceState(outState: Bundle) {
        saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    protected open fun saveInstanceState(outState: Bundle) {
        // Needs to be empty
    }

    protected open fun restoreInstanceState(savedState: Bundle) {
        // Needs to be empty
    }

    protected fun showError(errorMessage: String) {
        view?.let { 
            ViewUtils.createSnackbar(it, errorMessage, Snackbar.LENGTH_SHORT).show()
        }
    }

    protected fun showSnackbarMessage(resId: Int) {
        view?.let {
            ViewUtils.createSnackbar(it, resId, Snackbar.LENGTH_SHORT).show()
        }
    }
}