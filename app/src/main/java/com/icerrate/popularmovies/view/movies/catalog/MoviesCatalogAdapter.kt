package com.icerrate.popularmovies.view.movies.catalog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.icerrate.popularmovies.R
import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.databinding.ItemMoviesCatalogBinding
import com.icerrate.popularmovies.view.common.LoadMoreBaseAdapter

/**
 * @author Ivan Cerrate.
 */
class MoviesCatalogAdapter @JvmOverloads constructor(
    movies: ArrayList<Movie> = arrayListOf(),
    private val onItemClickListener: OnItemClickListener?,
    private val columns: Int
) : LoadMoreBaseAdapter<Movie>() {

    init {
        this.data = movies.toMutableList()
    }

    override fun onCreateDataViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemMoviesCatalogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.root.layoutParams.height = (parent.width / columns * RATIO).toInt()
        return MovieViewHolder(binding, onItemClickListener)
    }

    override fun onBindDataViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MovieViewHolder) {
            val movie = data[position] as Movie
            holder.bind(movie)
        }
    }

    fun addItems(items: List<Movie>) {
        this.data.addAll(items)
        notifyDataSetChanged()
    }

    fun resetItems() {
        this.data.clear()
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(
        private val binding: ItemMoviesCatalogBinding,
        private val onItemClickListener: OnItemClickListener?
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(movie: Movie) {
            val context = binding.root.context
            val url = movie.getPosterUrl("w500")

            Glide.with(context)
                .load(url)
                .placeholder(ContextCompat.getDrawable(context, R.drawable.poster_placeholder))
                .error(ContextCompat.getDrawable(context, R.drawable.poster_placeholder))
                .into(binding.poster)

            binding.root.tag = movie
        }

        override fun onClick(view: View) {
            onItemClickListener?.onItemClick(view)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(view: View)
    }

    companion object {
        private const val RATIO = 1.5f
    }
}