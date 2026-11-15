package com.mangalibrary.Fragmentos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.material.chip.Chip
import com.mangalibrary.R
import com.mangalibrary.data.local.FavoritesManager
import com.mangalibrary.data.local.ReadingHistoryManager
import com.mangalibrary.data.model.ChapterItem
import com.mangalibrary.databinding.FragmentMangaDetailBinding
import com.mangalibrary.ui.LectorActivity
import com.mangalibrary.ui.MangaViewModel
import com.mangalibrary.ui.adapters.ChapterAdapter
import com.mangalibrary.ui.adapters.RecommendationAdapter
import kotlinx.coroutines.launch

class FragmentDetalleManga : Fragment() {

    private var _binding: FragmentMangaDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MangaViewModel by activityViewModels()
    private lateinit var favoritesManager: FavoritesManager
    private lateinit var historyManager: ReadingHistoryManager
    private lateinit var chapterAdapter: ChapterAdapter
    private lateinit var recAdapter: RecommendationAdapter

    private var allChapters: List<ChapterItem> = emptyList()
    private var currentLanguageFilter: String = "es" // Default preferred language
    private var isAscendingSort = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMangaDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesManager = FavoritesManager(requireContext())
        historyManager = ReadingHistoryManager(requireContext())

        // Load preferred language from user profile
        val sharedPrefs = requireContext().getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE)
        currentLanguageFilter = sharedPrefs.getString("preferred_lang", "es") ?: "es"

        binding.toolbarDetail.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        chapterAdapter = ChapterAdapter()
        binding.rvChapters.layoutManager = LinearLayoutManager(requireContext())
        binding.rvChapters.adapter = chapterAdapter

        recAdapter = RecommendationAdapter { rec ->
            viewModel.selectManga(rec.manga)
        }
        binding.rvRecommendations.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecommendations.adapter = recAdapter

        binding.toggleGroupSort.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                isAscendingSort = (checkedId == R.id.btnSortAsc)
                applyChapterLanguageFilter()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    val manga = state.selectedManga ?: return@collect
                    chapterAdapter.mangaId = manga.id

                    binding.tvDetailTitle.text = manga.title
                    binding.tvDetailAuthor.text = if (manga.authors.isNotEmpty()) {
                        "Autor: ${manga.authors.joinToString(", ") { it.name }}"
                    } else ""
                    binding.tvDetailStatus.text = "Estado: ${manga.status.replaceFirstChar { it.uppercase() }}"
                    binding.tvDetailRating.text = "Clasificación: ${manga.contentRating.replaceFirstChar { it.uppercase() }}"
                    binding.tvDetailYear.text = manga.year?.let { "Año: $it" } ?: ""

                    binding.tvDescription.text = manga.description

                    binding.ivCover.load(manga.coverUrl) {
                        crossfade(true)
                    }

                    // Botón Continuar Leyendo
                    updateContinueReadingButton(manga.id)

                    // Favorite button
                    val isFav = favoritesManager.isFavorite(manga.id)
                    binding.btnFavorite.setImageResource(
                        if (isFav) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off
                    )

                    binding.btnFavorite.setOnClickListener {
                        val newFavStatus = favoritesManager.toggleFavorite(manga)
                        binding.btnFavorite.setImageResource(
                            if (newFavStatus) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off
                        )
                        val msg = if (newFavStatus) "Añadido a Favoritos" else "Eliminado de Favoritos"
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                    }

                    binding.cgTags.removeAllViews()
                    (manga.genres + manga.tags).forEach { tag ->
                        val chip = Chip(requireContext()).apply {
                            text = tag.name
                            isClickable = true
                            isCheckable = false
                            setOnClickListener {
                                viewModel.setFilters(null, tag.id, tag.name)
                                viewModel.searchManga()
                                parentFragmentManager.beginTransaction()
                                    .replace(R.id.FragmentL1, FragmentBusqueda())
                                    .addToBackStack(null)
                                    .commit()
                            }
                        }
                        binding.cgTags.addView(chip)
                    }

                    recAdapter.submitList(state.recommendations)

                    binding.pbChapters.visibility = if (state.isDetailLoading) View.VISIBLE else View.GONE
                    allChapters = state.chapters
                    setupLanguageDropdown()
                    applyChapterLanguageFilter()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh preferred language in case it was updated in profile
        val sharedPrefs = requireContext().getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE)
        val newPrefLang = sharedPrefs.getString("preferred_lang", "es") ?: "es"
        if (newPrefLang != currentLanguageFilter && allChapters.isNotEmpty()) {
            currentLanguageFilter = newPrefLang
            setupLanguageDropdown()
            applyChapterLanguageFilter()
        }

        viewModel.uiState.value.selectedManga?.let { manga ->
            updateContinueReadingButton(manga.id)
        }
    }

    private fun updateContinueReadingButton(mangaId: String) {
        val lastRead = historyManager.getLastReadChapter(mangaId)
        if (lastRead != null) {
            binding.btnContinueReading.visibility = View.VISIBLE
            var displayTitle = lastRead.chapterTitle
                .replace("Capítulo -: ", "")
                .replace("Capítulo - : ", "")
            if (displayTitle.contains("Capítulo Capítulo")) {
                displayTitle = displayTitle.replace("Capítulo Capítulo", "Capítulo")
            }
            binding.btnContinueReading.text = "📖 Continuar Leyendo: $displayTitle"
            binding.btnContinueReading.setOnClickListener {
                val filtered = getFilteredAndSortedChapters()
                val chapterIds = ArrayList(filtered.map { it.id })
                val chapterTitles = ArrayList(filtered.map { c ->
                    "Capítulo ${c.chapter ?: "-"}${c.title?.let { t -> ": $t" } ?: ""}"
                })
                val index = filtered.indexOfFirst { it.id == lastRead.chapterId }

                val intent = Intent(requireContext(), LectorActivity::class.java).apply {
                    putExtra("CHAPTER_ID", lastRead.chapterId)
                    putExtra("CHAPTER_TITLE", lastRead.chapterTitle)
                    putExtra("MANGA_ID", mangaId)
                    putExtra("CHAPTER_INDEX", if (index != -1) index else 0)
                    putStringArrayListExtra("CHAPTER_IDS", chapterIds)
                    putStringArrayListExtra("CHAPTER_TITLES", chapterTitles)
                }
                startActivity(intent)
            }
        } else {
            binding.btnContinueReading.visibility = View.GONE
        }
    }

    private fun setupLanguageDropdown() {
        val langs = listOf("Todos") + allChapters.map { it.translatedLanguage.lowercase() }.distinct().sorted()
        val langAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, langs)
        binding.autoCompleteLang.setAdapter(langAdapter)

        // Set selection in dropdown matching currentLanguageFilter
        val displaySelected = if (currentLanguageFilter == "all") "Todos" else currentLanguageFilter
        binding.autoCompleteLang.setText(displaySelected, false)

        binding.autoCompleteLang.setOnItemClickListener { _, _, position, _ ->
            val selected = langs[position]
            currentLanguageFilter = if (selected == "Todos") "all" else selected
            applyChapterLanguageFilter()
        }
    }

    private fun getFilteredAndSortedChapters(): List<ChapterItem> {
        var filtered = if (currentLanguageFilter == "all") {
            allChapters
        } else {
            allChapters.filter { it.translatedLanguage.lowercase() == currentLanguageFilter }
        }
        filtered = if (isAscendingSort) {
            filtered.sortedBy { it.chapter?.toFloatOrNull() ?: 0f }
        } else {
            filtered.sortedByDescending { it.chapter?.toFloatOrNull() ?: 0f }
        }
        return filtered
    }

    private fun applyChapterLanguageFilter() {
        val filtered = getFilteredAndSortedChapters()
        chapterAdapter.submitList(filtered)

        if (allChapters.isEmpty() && !viewModel.uiState.value.isDetailLoading) {
            binding.tvNoChapters.visibility = View.VISIBLE
            binding.tvNoChapters.text = "No hay capítulos disponibles para este manga."
        } else if (filtered.isEmpty() && !viewModel.uiState.value.isDetailLoading) {
            binding.tvNoChapters.visibility = View.VISIBLE
            binding.tvNoChapters.text = "No hay capítulos disponibles en este idioma."
        } else {
            binding.tvNoChapters.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
