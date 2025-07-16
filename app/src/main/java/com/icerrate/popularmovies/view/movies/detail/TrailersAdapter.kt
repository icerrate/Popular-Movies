package com.icerrate.popularmovies.view.movies.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.icerrate.popularmovies.R
import com.icerrate.popularmovies.data.model.Trailer
import com.icerrate.popularmovies.databinding.ItemTrailerBinding

/**
 * @author Ivan Cerrate.
 */
class TrailersAdapter(
    private val items: MutableList<Trailer> = mutableListOf(),
    private val onItemClickListener: OnItemClickListener?
) : RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        val binding = ItemTrailerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrailerViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(viewHolder: TrailerViewHolder, position: Int) {
        val trailer = items[position]
        viewHolder.bind(trailer)
    }

    override fun getItemCount(): Int = items.size

    fun addItems(newItems: List<Trailer>) {
        this.items.addAll(newItems)
        notifyDataSetChanged()
    }

    class TrailerViewHolder(
        private val binding: ItemTrailerBinding,
        private val onItemClickListener: OnItemClickListener?
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
            binding.play.setOnClickListener(this)
        }

        fun bind(trailer: Trailer) {
            val context = itemView.context
            val url = trailer.getVideoThumbnail()

            Glide.with(context)
                .load(url)
                .placeholder(ContextCompat.getDrawable(context, R.drawable.backdrop_placeholder))
                .into(binding.thumbnail)

            itemView.tag = trailer
            binding.play.tag = trailer
        }

        override fun onClick(view: View) {
            onItemClickListener?.onItemClick(view)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(view: View)
    }
}