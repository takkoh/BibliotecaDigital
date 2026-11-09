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
import com.mangalibrary.ui.LectorActivity

class ChapterAdapter(
    var mangaId: String? = null,
    private val onChapterClicked: ((ChapterItem) -> Unit)? = null
) : ListAdapter<ChapterItem, ChapterAdapter.ChapterViewHolder>(ChapterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val binding = ItemChapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChapterViewHolder(binding, ::getItem, ::getItemCount, mangaId, onChapterClicked)
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    class ChapterViewHolder(
        private val binding: ItemChapterBinding,
        private val getItemAt: (Int) -> ChapterItem,
        private val getItemCount: () -> Int,
        private val mangaId: String?,
        private val onChapterClicked: ((ChapterItem) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chapter: ChapterItem, position: Int) {
            val titleText = "Capítulo ${chapter.chapter ?: "-"}${chapter.title?.let { ": $it" } ?: ""}"
            binding.tvChapterTitle.text = titleText
            binding.tvChapterLang.text = "Idioma: ${chapter.translatedLanguage.uppercase()}"
            if (!chapter.scanlationGroup.isNullOrBlank()) {
                binding.tvChapterGroup.visibility = View.VISIBLE
                binding.tvChapterGroup.text = "Traductor: ${chapter.scanlationGroup}"
            } else {
                binding.tvChapterGroup.visibility = View.GONE
            }

            binding.root.setOnClickListener {
                onChapterClicked?.invoke(chapter)

                val context = binding.root.context
                val count = getItemCount()
                val chapterIds = ArrayList((0 until count).map { getItemAt(it).id })
                val chapterTitles = ArrayList((0 until count).map {
                    val c = getItemAt(it)
                    "Capítulo ${c.chapter ?: "-"}${c.title?.let { t -> ": $t" } ?: ""}"
                })

                val intent = Intent(context, LectorActivity::class.java).apply {
                    putExtra("CHAPTER_ID", chapter.id)
                    putExtra("CHAPTER_TITLE", titleText)
                    putExtra("MANGA_ID", mangaId)
                    putExtra("CHAPTER_INDEX", position)
                    putStringArrayListExtra("CHAPTER_IDS", chapterIds)
                    putStringArrayListExtra("CHAPTER_TITLES", chapterTitles)
                }
                context.startActivity(intent)
            }

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
