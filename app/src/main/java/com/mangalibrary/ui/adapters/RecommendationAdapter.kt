package com.mangalibrary.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mangalibrary.data.model.RecommendationResult
import com.mangalibrary.databinding.ItemRecommendationBinding

class RecommendationAdapter(
    private val onRecClick: (RecommendationResult) -> Unit
) : ListAdapter<RecommendationResult, RecommendationAdapter.RecommendationViewHolder>(RecDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val binding = ItemRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendationViewHolder(binding, onRecClick)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RecommendationViewHolder(
        private val binding: ItemRecommendationBinding,
        private val onRecClick: (RecommendationResult) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(rec: RecommendationResult) {
            binding.tvRecTitle.text = rec.manga.title
            binding.tvRecScore.text = "★ ${rec.score} pts"

            binding.ivRecCover.load(rec.manga.coverUrl) {
                crossfade(true)
            }

            binding.root.setOnClickListener {
                onRecClick(rec)
            }
        }
    }

    class RecDiffCallback : DiffUtil.ItemCallback<RecommendationResult>() {
        override fun areItemsTheSame(oldItem: RecommendationResult, newItem: RecommendationResult): Boolean =
            oldItem.manga.id == newItem.manga.id

        override fun areContentsTheSame(oldItem: RecommendationResult, newItem: RecommendationResult): Boolean =
            oldItem == newItem
    }
}
