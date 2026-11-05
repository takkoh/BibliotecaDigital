package com.mangalibrary.Fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mangalibrary.R
import com.mangalibrary.data.local.FavoritesManager
import com.mangalibrary.databinding.FragmentRecommendationsBinding
import com.mangalibrary.domain.RecommendationEngine
import com.mangalibrary.ui.MangaViewModel
import com.mangalibrary.ui.adapters.MangaAdapter
import kotlinx.coroutines.launch

class FragmentRecomendaciones : Fragment() {

    private var _binding: FragmentRecommendationsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MangaViewModel by activityViewModels()
    private lateinit var favoritesManager: FavoritesManager
    private val recommendationEngine = RecommendationEngine()
    private lateinit var adapter: MangaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecommendationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesManager = FavoritesManager(requireContext())

        adapter = MangaAdapter { manga ->
            viewModel.selectManga(manga)
            parentFragmentManager.beginTransaction()
                .replace(R.id.FragmentL1, FragmentDetalleManga())
                .addToBackStack(null)
                .commit()
        }

        binding.rvTabRecommendations.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTabRecommendations.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    val favorites = favoritesManager.getFavorites()
                    val candidates = state.searchResults

                    val recList = if (favorites.isNotEmpty() && candidates.isNotEmpty()) {
                        val baseTarget = favorites.first()
                        recommendationEngine.getRecommendations(baseTarget, candidates, limit = 20)
                            .map { it.manga }
                    } else {
                        candidates
                    }

                    adapter.submitList(recList)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
