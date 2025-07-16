package com.icerrate.popularmovies.view.movies.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.icerrate.popularmovies.data.model.Review
import com.icerrate.popularmovies.databinding.ItemReviewBinding

/**
 * @author Ivan Cerrate.
 */
class ReviewsAdapter @JvmOverloads constructor(
    private val items: MutableList<Review> = mutableListOf(),
    private val onButtonClickListener: OnButtonClickListener?
) : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding, onButtonClickListener)
    }

    override fun onBindViewHolder(viewHolder: ReviewViewHolder, position: Int) {
        val review = items[position]
        viewHolder.binding(review)
    }

    override fun getItemCount(): Int = items.size

    fun addItems(newItems: ArrayList<Review>) {
        this.items.addAll(newItems)
        notifyDataSetChanged()
    }

    class ReviewViewHolder(
        private val binding: ItemReviewBinding,
        private val onButtonClickListener: OnButtonClickListener?
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {


        init {
            binding.visit.setOnClickListener(this)
        }

        fun binding(review: Review) {
            binding.author.text = review.author
            binding.content.text = review.content
            binding.visit.tag = review
        }

        override fun onClick(view: View) {
            onButtonClickListener?.onPlayButtonClick(view)
        }
    }

    interface OnButtonClickListener {
        fun onPlayButtonClick(view: View)
    }
}