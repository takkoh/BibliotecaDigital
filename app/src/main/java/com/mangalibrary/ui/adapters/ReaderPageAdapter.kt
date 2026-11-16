package com.mangalibrary.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mangalibrary.databinding.ItemReaderPageBinding

/**
 * Adaptador para mostrar las páginas del capítulo en un ViewPager2 o RecyclerView (Webtoon).
 * Utiliza ListAdapter y Coil para cargar eficientemente las imágenes de cada página.
 */
class ReaderPageAdapter(
    private val isWebtoon: Boolean = false,
    private val onPageClicked: (() -> Unit)? = null
) : ListAdapter<String, ReaderPageAdapter.PageViewHolder>(PageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val binding = ItemReaderPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        if (isWebtoon) {
            binding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            binding.ivChapterPage.adjustViewBounds = true
        } else {
            binding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            binding.ivChapterPage.adjustViewBounds = false
        }
        return PageViewHolder(binding, onPageClicked)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PageViewHolder(
        private val binding: ItemReaderPageBinding,
        private val onPageClicked: (() -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUrl: String) {
            binding.ivChapterPage.load(imageUrl) {
                crossfade(true)
            }
            binding.root.setOnClickListener {
                onPageClicked?.invoke()
            }
        }
    }

    class PageDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
    }
}
