package com.mangalibrary.Fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.material.chip.Chip
import com.mangalibrary.data.local.FavoritesManager
import com.mangalibrary.databinding.FragmentMangaDetailBinding
import com.mangalibrary.ui.MangaViewModel
import com.mangalibrary.ui.adapters.ChapterAdapter
import com.mangalibrary.ui.adapters.RecommendationAdapter
import kotlinx.coroutines.launch

class FragmentDetalleManga : Fragment() {

    private var _binding: FragmentMangaDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MangaViewModel by activityViewModels()
    private lateinit var favoritesManager: FavoritesManager
    private lateinit var chapterAdapter: ChapterAdapter
    private lateinit var recAdapter: RecommendationAdapter

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

        chapterAdapter = ChapterAdapter()
        binding.rvChapters.layoutManager = LinearLayoutManager(requireContext())
        binding.rvChapters.adapter = chapterAdapter

        recAdapter = RecommendationAdapter { rec ->
            viewModel.selectManga(rec.manga)
        }
        binding.rvRecommendations.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecommendations.adapter = recAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    val manga = state.selectedManga ?: return@collect

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

                    // Update favorite star icon
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
                            isClickable = false
                            isCheckable = false
                        }
                        binding.cgTags.addView(chip)
                    }

                    recAdapter.submitList(state.recommendations)

                    binding.pbChapters.visibility = if (state.isDetailLoading) View.VISIBLE else View.GONE
                    chapterAdapter.submitList(state.chapters)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
