package com.icerrate.popularmovies.view.common

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.icerrate.popularmovies.R

/**
 * @author Ivan Cerrate.
 */
abstract class LoadMoreBaseAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var hasLoadingFooter = false
    protected var data: MutableList<T?> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_LOAD) {
            val progressView =
                LayoutInflater.from(parent.context).inflate(R.layout.footer_space, parent, false)
            ProgressViewHolder(progressView)
        } else {
            onCreateDataViewHolder(parent, viewType)
        }
    }

    abstract fun onCreateDataViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_DATA) {
            onBindDataViewHolder(holder, position)
        }
    }

    abstract fun onBindDataViewHolder(holder: RecyclerView.ViewHolder, position: Int)

    override fun getItemViewType(position: Int): Int {
        return if (data[position] != null) VIEW_TYPE_DATA else VIEW_TYPE_LOAD
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun getItem(index: Int): T {
        return data[index]
            ?: throw IllegalArgumentException("Item with index $index doesn't exist, dataSet is $data")
    }

    fun isLoading(): Boolean {
        return hasLoadingFooter
    }

    fun setLoading(loading: Boolean) {
        val handler = Handler(Looper.getMainLooper())

        if (loading) {
            hasLoadingFooter = true
            if (!data.contains(null)) {
                data.add(null)
                handler.post {
                    notifyItemInserted(data.size - 1)
                }
            }
        } else {
            hasLoadingFooter = false
            val nullIndex = data.indexOf(null)
            if (nullIndex != -1) {
                data.removeAt(nullIndex)
                handler.post {
                    notifyItemRemoved(nullIndex)
                }
            }
        }
        handler.post {
            notifyDataSetChanged()
        }
    }

    companion object {
        protected const val VIEW_TYPE_LOAD = 1
        protected const val VIEW_TYPE_DATA = 2
    }
}