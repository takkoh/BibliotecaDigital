package com.mangalibrary.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.mangalibrary.data.local.ReadingHistoryManager
import com.mangalibrary.data.model.ChapterItem
import com.mangalibrary.data.repository.MangaDexRepository
import com.mangalibrary.databinding.ActivityLectorBinding
import com.mangalibrary.ui.adapters.ReaderPageAdapter
import kotlinx.coroutines.launch

class LectorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLectorBinding
    private val repository = MangaDexRepository()
    private lateinit var historyManager: ReadingHistoryManager
    private lateinit var adapter: ReaderPageAdapter

    private var currentChapterId: String? = null
    private var currentChapterTitle: String? = null
    private var mangaId: String? = null
    private var currentIndex: Int = -1
    private var chapterIds: ArrayList<String> = arrayListOf()
    private var chapterTitles: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        historyManager = ReadingHistoryManager(this)

        currentChapterId = intent.getStringExtra("CHAPTER_ID")
        currentChapterTitle = intent.getStringExtra("CHAPTER_TITLE") ?: "Lector de Manga"
        mangaId = intent.getStringExtra("MANGA_ID")
        currentIndex = intent.getIntExtra("CHAPTER_INDEX", -1)
        chapterIds = intent.getStringArrayListExtra("CHAPTER_IDS") ?: arrayListOf()
        chapterTitles = intent.getStringArrayListExtra("CHAPTER_TITLES") ?: arrayListOf()

        setSupportActionBar(binding.toolbarReader)
        supportActionBar?.apply {
            title = currentChapterTitle
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.toolbarReader.setNavigationOnClickListener {
            finish()
        }

        adapter = ReaderPageAdapter()
        binding.viewPagerPages.adapter = adapter

        binding.viewPagerPages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val total = adapter.itemCount
                if (total > 0) {
                    binding.tvPageIndicator.text = "${position + 1} / $total"
                } else {
                    binding.tvPageIndicator.text = ""
                }
            }
        })

        actualizarBotonesNavegacion()

        binding.btnPrevChapter.setOnClickListener {
            if (currentIndex > 0) {
                cargarCapituloEnIndice(currentIndex - 1)
            }
        }

        binding.btnNextChapter.setOnClickListener {
            if (currentIndex in 0 until chapterIds.size - 1) {
                cargarCapituloEnIndice(currentIndex + 1)
            }
        }

        val chId = currentChapterId
        if (chId.isNullOrBlank()) {
            Toast.makeText(this, "ID de capítulo no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        guardarHistorial(chId, currentChapterTitle ?: "")
        cargarPaginasCapitulo(chId)
    }

    private fun cargarCapituloEnIndice(newIndex: Int) {
        if (newIndex in 0 until chapterIds.size) {
            currentIndex = newIndex
            currentChapterId = chapterIds[newIndex]
            currentChapterTitle = if (newIndex in 0 until chapterTitles.size) chapterTitles[newIndex] else "Lector de Manga"

            supportActionBar?.title = currentChapterTitle
            actualizarBotonesNavegacion()

            currentChapterId?.let { chId ->
                guardarHistorial(chId, currentChapterTitle ?: "")
                cargarPaginasCapitulo(chId)
            }
        }
    }

    private fun actualizarBotonesNavegacion() {
        if (chapterIds.isEmpty() || currentIndex == -1) {
            binding.btnPrevChapter.visibility = View.GONE
            binding.btnNextChapter.visibility = View.GONE
            return
        }

        binding.btnPrevChapter.visibility = if (currentIndex > 0) View.VISIBLE else View.INVISIBLE
        binding.btnNextChapter.visibility = if (currentIndex < chapterIds.size - 1) View.VISIBLE else View.INVISIBLE
    }

    private fun guardarHistorial(chapterId: String, title: String) {
        val mId = mangaId ?: return
        val item = ChapterItem(
            id = chapterId,
            chapter = null,
            title = title,
            volume = null,
            translatedLanguage = "es",
            publishAt = null,
            externalUrl = null
        )
        historyManager.saveLastReadChapter(mId, item)
    }

    private fun cargarPaginasCapitulo(chapterId: String) {
        binding.tvPageIndicator.text = "Cargando..."
        lifecycleScope.launch {
            val result = repository.getChapterPages(chapterId)
            result.onSuccess { urls ->
                if (urls.isEmpty()) {
                    binding.tvPageIndicator.text = "Sin páginas"
                    Toast.makeText(this@LectorActivity, "No se encontraron páginas para este capítulo", Toast.LENGTH_SHORT).show()
                } else {
                    adapter.submitList(urls) {
                        binding.tvPageIndicator.text = "1 / ${urls.size}"
                    }
                }
            }.onFailure { error ->
                binding.tvPageIndicator.text = "Error"
                Toast.makeText(
                    this@LectorActivity,
                    "Error al cargar las páginas del capítulo: ${error.localizedMessage ?: "Error de red"}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
