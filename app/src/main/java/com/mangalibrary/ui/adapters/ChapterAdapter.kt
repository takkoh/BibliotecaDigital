package com.mangalibrary.ui.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mangalibrary.data.model.ChapterItem
import com.mangalibrary.databinding.ItemChapterBinding

class ChapterAdapter : ListAdapter<ChapterItem, ChapterAdapter.ChapterViewHolder>(ChapterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val binding = ItemChapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ChapterViewHolder(
        private val binding: ItemChapterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chapter: ChapterItem) {
            val titleText = "Capítulo ${chapter.chapter ?: "-"}${chapter.title?.let { ": $it" } ?: ""}"
            binding.tvChapterTitle.text = titleText
            binding.tvChapterLang.text = "Idioma: ${chapter.translatedLanguage.uppercase()}"

            // Configurar el clic en todo el elemento para abrir el lector interno de la app
            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, com.mangalibrary.ui.LectorActivity::class.java).apply {
                    putExtra("CHAPTER_ID", chapter.id)
                    putExtra("CHAPTER_TITLE", titleText)
                }
                context.startActivity(intent)
            }

            // Opcional: mantener enlace externo si está disponible
            if (!chapter.externalUrl.isNullOrBlank()) {
                binding.btnOpenChapter.visibility = View.VISIBLE
                binding.btnOpenChapter.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(chapter.externalUrl))
                    binding.root.context.startActivity(intent)
                }
            } else {
                binding.btnOpenChapter.visibility = View.GONE
            }
        }
    }

    class ChapterDiffCallback : DiffUtil.ItemCallback<ChapterItem>() {
        override fun areItemsTheSame(oldItem: ChapterItem, newItem: ChapterItem): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ChapterItem, newItem: ChapterItem): Boolean = oldItem == newItem
    }
}
