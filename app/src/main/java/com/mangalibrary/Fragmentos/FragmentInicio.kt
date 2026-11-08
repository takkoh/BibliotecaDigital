package com.mangalibrary.Fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.mangalibrary.R
import com.mangalibrary.databinding.FragmentMangaSearchBinding
import com.mangalibrary.ui.MangaViewModel
import com.mangalibrary.ui.adapters.MangaAdapter
import kotlinx.coroutines.launch

class FragmentInicio : Fragment() {

    private var _binding: FragmentMangaSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MangaViewModel by activityViewModels()
    private lateinit var adapter: MangaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMangaSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MangaAdapter { selectedManga ->
            viewModel.selectManga(selectedManga)
            parentFragmentManager.beginTransaction()
                .replace(R.id.FragmentL1, FragmentDetalleManga())
                .addToBackStack(null)
                .commit()
        }

        binding.rvMangaSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMangaSearch.adapter = adapter

        setupFilterChips()

        binding.etSearch.doAfterTextChanged { text ->
            viewModel.onSearchQueryChanged(text?.toString() ?: "")
        }

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.searchManga()
                true
            } else {
                false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    adapter.submitList(state.searchResults)

                    binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE

                    if (!state.isLoading && state.searchResults.isEmpty()) {
                        binding.tvEmptyError.visibility = View.VISIBLE
                        binding.tvEmptyError.text = state.errorMessage ?: "No se encontraron mangas."
                    } else {
                        binding.tvEmptyError.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setupFilterChips() {
        binding.cgStatusFilter.setOnCheckedStateChangeListener { _, checkedIds ->
            val checkedId = checkedIds.firstOrNull() ?: R.id.chip_status_all
            val status = when (checkedId) {
                R.id.chip_status_ongoing -> "ongoing"
                R.id.chip_status_completed -> "completed"
                else -> null
            }
            viewModel.setFilters(status, viewModel.uiState.value.selectedGenreTagId)
        }

        binding.cgGenreFilter.setOnCheckedStateChangeListener { _, checkedIds ->
            val checkedId = checkedIds.firstOrNull() ?: R.id.chip_genre_all
            val tagId = if (checkedId != R.id.chip_genre_all) {
                val chip = binding.cgGenreFilter.findViewById<Chip>(checkedId)
                chip?.tag as? String
            } else null

            viewModel.setFilters(viewModel.uiState.value.selectedStatus, tagId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
