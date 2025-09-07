package com.albert.capstoneproject.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albert.capstoneproject.Data.model.Product
import com.albert.capstoneproject.databinding.ProductDetailCommentsListItemBinding

class CommentsAdapter(
    private var comments: List<Product>
) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ProductDetailCommentsListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductDetailCommentsListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]

        holder.binding.tvCommentName.text = comment.title
        holder.binding.ivCommentImage.setImageResource(comment.image.toInt())
        holder.binding.tvComment.text = comment.description
        val rate = comment.rating.rate.toFloat() ?: 0f
        holder.binding.rating.rating = rate
    }
}