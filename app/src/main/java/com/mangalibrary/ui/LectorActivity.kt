package com.mangalibrary.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var webtoonAdapter: ReaderPageAdapter

    private var isWebtoonMode = false
    private var isControlsVisible = true

    private var currentChapterId: String? = null
    private var currentChapterTitle: String? = null
    private var mangaId: String? = null
    private var currentIndex: Int = -1
    private var chapterIds: ArrayList<String> = arrayListOf()
    private var chapterTitles: ArrayList<String> = arrayListOf()
    private var chapterNumbers: ArrayList<String> = arrayListOf()
    private var chapterLanguages: ArrayList<String> = arrayListOf()
    private var chapterGroups: ArrayList<String> = arrayListOf()

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
        chapterNumbers = intent.getStringArrayListExtra("CHAPTER_NUMBERS") ?: arrayListOf()
        chapterLanguages = intent.getStringArrayListExtra("CHAPTER_LANGUAGES") ?: arrayListOf()
        chapterGroups = intent.getStringArrayListExtra("CHAPTER_GROUPS") ?: arrayListOf()

        deduplicateChapters()

        setSupportActionBar(binding.toolbarReader)
        supportActionBar?.apply {
            title = currentChapterTitle
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.toolbarReader.setNavigationOnClickListener {
            finish()
        }

        // Listener para alternar modo inmersivo pantalla completa al tocar páginas
        val onPageClickLambda = {
            toggleControlsAndImmersion()
        }

        adapter = ReaderPageAdapter(isWebtoon = false, onPageClicked = onPageClickLambda)
        binding.viewPagerPages.adapter = adapter

        webtoonAdapter = ReaderPageAdapter(isWebtoon = true, onPageClicked = onPageClickLambda)
        binding.rvWebtoonPages.layoutManager = LinearLayoutManager(this)
        binding.rvWebtoonPages.adapter = webtoonAdapter

        // Scroll listener para actualizar contador de páginas en modo Webtoon
        binding.rvWebtoonPages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isWebtoonMode) {
                    val lm = binding.rvWebtoonPages.layoutManager as? LinearLayoutManager
                    val firstVisible = lm?.findFirstVisibleItemPosition() ?: 0
                    val total = webtoonAdapter.itemCount
                    if (total > 0) {
                        binding.tvPageIndicator.text = "${firstVisible + 1} / $total"
                    }
                }
            }
        })

        // Alternar modo Webtoon / Páginas
        binding.btnToggleMode.setOnClickListener {
            isWebtoonMode = !isWebtoonMode
            if (isWebtoonMode) {
                binding.viewPagerPages.visibility = View.GONE
                binding.rvWebtoonPages.visibility = View.VISIBLE
                binding.btnToggleMode.text = "← Páginas"
                val total = webtoonAdapter.itemCount
                if (total > 0) {
                    val lm = binding.rvWebtoonPages.layoutManager as? LinearLayoutManager
                    val firstVisible = lm?.findFirstVisibleItemPosition() ?: 0
                    binding.tvPageIndicator.text = "${firstVisible + 1} / $total"
                }
            } else {
                binding.viewPagerPages.visibility = View.VISIBLE
                binding.rvWebtoonPages.visibility = View.GONE
                binding.btnToggleMode.text = "↕ Webtoon"
                val total = adapter.itemCount
                if (total > 0) {
                    val currentPos = binding.viewPagerPages.currentItem
                    binding.tvPageIndicator.text = "${currentPos + 1} / $total"
                }
            }
        }

        binding.viewPagerPages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (!isWebtoonMode) {
                    val total = adapter.itemCount
                    if (total > 0) {
                        binding.tvPageIndicator.text = "${position + 1} / $total"
                    } else {
                        binding.tvPageIndicator.text = ""
                    }
                }
            }
        })

        actualizarBotonesNavegacion()

        // Navegación inteligente de capítulos (salta al siguiente número único evitando duplicados de traductores)
        binding.btnPrevChapter.setOnClickListener {
            val prevIndex = findNextUniqueChapterIndex(currentIndex, forward = false)
            if (prevIndex != -1) {
                cargarCapituloEnIndice(prevIndex)
            }
        }

        binding.btnNextChapter.setOnClickListener {
            val nextIndex = findNextUniqueChapterIndex(currentIndex, forward = true)
            if (nextIndex != -1) {
                cargarCapituloEnIndice(nextIndex)
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

    private fun extractChapterFloat(raw: String?): Float? {
        if (raw.isNullOrBlank()) return null
        raw.trim().toFloatOrNull()?.let { return it }
        val match = Regex("""\d+(?:\.\d+)?""").find(raw)
        return match?.value?.toFloatOrNull()
    }

    private fun deduplicateChapters() {
        if (chapterNumbers.isEmpty() || chapterIds.isEmpty() || chapterIds.size != chapterNumbers.size) return

        val targetChapterId = if (currentIndex in chapterIds.indices) chapterIds[currentIndex] else currentChapterId
        val targetIndex = chapterIds.indexOf(targetChapterId)
        val targetLang = if (targetIndex in chapterLanguages.indices) chapterLanguages[targetIndex] else ""

        val uniqueMap = LinkedHashMap<String, Int>()

        for (i in chapterNumbers.indices) {
            val lang = if (i in chapterLanguages.indices) chapterLanguages[i] else ""
            if (targetLang.isNotBlank() && lang.isNotBlank() && !lang.equals(targetLang, ignoreCase = true)) {
                continue
            }

            val rawNum = chapterNumbers[i]
            val lower = rawNum.lowercase()
            val floatVal = extractChapterFloat(rawNum)

            val prefix = when {
                lower.contains("extra") || lower.contains("ex") -> "extra_"
                lower.contains("special") || lower.contains("sp") -> "special_"
                lower.contains("omake") -> "omake_"
                lower.contains("oneshot") -> "oneshot_"
                lower.contains("side") -> "side_"
                else -> ""
            }

            val key = if (floatVal != null) {
                "$prefix$floatVal"
            } else {
                rawNum.trim().lowercase().ifBlank { chapterIds[i] }
            }

            if (!uniqueMap.containsKey(key)) {
                uniqueMap[key] = i
            } else {
                val existingIndex = uniqueMap[key]!!
                val existingGroup = if (existingIndex in chapterGroups.indices) chapterGroups[existingIndex] else ""
                val currentGame = if (i in chapterGroups.indices) chapterGroups[i] else ""
                // Priorizar grupo no vacío o el grupo actual si coincide con el capítulo inicial
                if (existingGroup.isBlank() && !currentGame.isBlank()) {
                    uniqueMap[key] = i
                }
            }
        }

        val newIds = arrayListOf<String>()
        val newTitles = arrayListOf<String>()
        val newNumbers = arrayListOf<String>()
        val newLangs = arrayListOf<String>()
        val newGroups = arrayListOf<String>()

        var newCurrentIndex = 0
        var foundTarget = false

        uniqueMap.values.forEach { originalIndex ->
            val id = chapterIds[originalIndex]
            if (id == targetChapterId) {
                newCurrentIndex = newIds.size
                foundTarget = true
            }
            newIds.add(id)
            newTitles.add(if (originalIndex in chapterTitles.indices) chapterTitles[originalIndex] else "")
            newNumbers.add(if (originalIndex in chapterNumbers.indices) chapterNumbers[originalIndex] else "")
            newLangs.add(if (originalIndex in chapterLanguages.indices) chapterLanguages[originalIndex] else "")
            newGroups.add(if (originalIndex in chapterGroups.indices) chapterGroups[originalIndex] else "")
        }

        chapterIds = newIds
        chapterTitles = newTitles
        chapterNumbers = newNumbers
        chapterLanguages = newLangs
        chapterGroups = newGroups
        if (foundTarget) {
            currentIndex = newCurrentIndex
        } else if (currentIndex >= chapterIds.size) {
            currentIndex = chapterIds.size - 1
        }
    }

    private fun findNextUniqueChapterIndex(current: Int, forward: Boolean): Int {
        if (forward) {
            return if (current + 1 < chapterIds.size) current + 1 else -1
        } else {
            return if (current - 1 >= 0) current - 1 else -1
        }
    }

    private fun toggleControlsAndImmersion() {
        isControlsVisible = !isControlsVisible
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        if (isControlsVisible) {
            binding.toolbarReader.visibility = View.VISIBLE
            binding.layoutBottomNav.visibility = View.VISIBLE
            controller.show(WindowInsetsCompat.Type.systemBars())
        } else {
            binding.toolbarReader.visibility = View.GONE
            binding.layoutBottomNav.visibility = View.GONE
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
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
        val cleanTitle = if (title.startsWith("Capítulo", ignoreCase = true)) title else "Capítulo: $title"
        val item = ChapterItem(
            id = chapterId,
            chapter = null,
            title = cleanTitle,
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
                    webtoonAdapter.submitList(urls)
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
