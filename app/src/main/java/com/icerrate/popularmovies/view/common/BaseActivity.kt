package com.icerrate.popularmovies.view.common

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import androidx.viewbinding.ViewBinding
import com.icerrate.popularmovies.R

/**
 * @author Ivan Cerrate.
 */

abstract class BaseActivity<V: ViewBinding> (
    private val inflate: (LayoutInflater) -> V
) : AppCompatActivity(), BaseFragmentListener {

    protected lateinit var binding: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.none, R.anim.fade_out)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun setNavigationToolbar(navigation: Boolean) {
        if (navigation && supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun replaceFragment(containerId: Int, fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(containerId, fragment)
        transaction.commit()
    }

    protected open fun saveInstanceState(outState: Bundle) {
        // Needs to be empty
    }

    protected open fun restoreInstanceState(savedState: Bundle) {
        // Needs to be empty
    }
}