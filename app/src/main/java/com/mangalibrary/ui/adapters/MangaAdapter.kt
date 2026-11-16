package com.mangalibrary.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.chip.Chip
import com.mangalibrary.data.model.MangaItem
import com.mangalibrary.databinding.ItemMangaBinding

class MangaAdapter(
    private val onMangaClick: (MangaItem) -> Unit
) : ListAdapter<MangaItem, MangaAdapter.MangaViewHolder>(MangaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MangaViewHolder {
        val binding = ItemMangaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MangaViewHolder(binding, onMangaClick)
    }

    override fun onBindViewHolder(holder: MangaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MangaViewHolder(
        private val binding: ItemMangaBinding,
        private val onMangaClick: (MangaItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(manga: MangaItem) {
            binding.tvMangaTitle.text = manga.title
            binding.tvMangaStatus.text = "Estado: ${manga.status.replaceFirstChar { it.uppercase() }}"
            binding.tvMangaAuthor.text = if (manga.authors.isNotEmpty()) {
                "Autor: ${manga.authors.joinToString(", ") { it.name }}"
            } else ""

            binding.ivMangaCover.load(manga.coverUrl) {
                crossfade(true)
            }

            binding.cgMangaGenres.removeAllViews()
            manga.genres.take(3).forEach { genre ->
                val chip = Chip(binding.root.context).apply {
                    text = genre.name
                    isClickable = false
                    isCheckable = false
                    textSize = 11f
                }
                binding.cgMangaGenres.addView(chip)
            }

            binding.root.setOnClickListener {
                onMangaClick(manga)
            }
        }
    }

    class MangaDiffCallback : DiffUtil.ItemCallback<MangaItem>() {
        override fun areItemsTheSame(oldItem: MangaItem, newItem: MangaItem): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MangaItem, newItem: MangaItem): Boolean = oldItem == newItem
    }
}
