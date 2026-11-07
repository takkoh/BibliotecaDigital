package com.mangalibrary.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mangalibrary.databinding.ItemReaderPageBinding

/**
 * Adaptador para mostrar las páginas del capítulo en un ViewPager2.
 * Utiliza ListAdapter y Coil para cargar eficientemente las imágenes de cada página.
 */
class ReaderPageAdapter : ListAdapter<String, ReaderPageAdapter.PageViewHolder>(PageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val binding = ItemReaderPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PageViewHolder(
        private val binding: ItemReaderPageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUrl: String) {
            // Carga la imagen de la página utilizando la librería Coil
            binding.ivChapterPage.load(imageUrl) {
                crossfade(true)
            }
        }
    }

    class PageDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
    }
}
