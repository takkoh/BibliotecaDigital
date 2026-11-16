package com.mangalibrary.Fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mangalibrary.R
import com.mangalibrary.databinding.FragmentBusquedaBinding
import com.mangalibrary.ui.MangaViewModel
import com.mangalibrary.ui.adapters.MangaAdapter
import kotlinx.coroutines.launch

class FragmentBusqueda : Fragment() {

    private var _binding: FragmentBusquedaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MangaViewModel by activityViewModels()
    private lateinit var searchAdapter: MangaAdapter

    private val statusOptions = listOf("Todos", "En Emisión", "Completado")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBusquedaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarBusqueda.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        searchAdapter = MangaAdapter { selectedManga ->
            viewModel.selectManga(selectedManga)
            parentFragmentManager.beginTransaction()
                .replace(R.id.FragmentL1, FragmentDetalleManga())
                .addToBackStack(null)
                .commit()
        }

        binding.rvSearchResults.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchResults.adapter = searchAdapter

        setupDropdown()

        binding.tilSearchBusqueda.setEndIconOnClickListener {
            binding.etSearchBusqueda.setText("")
            viewModel.setFilters(null, viewModel.uiState.value.selectedGenreTagId, viewModel.uiState.value.selectedGenreTagName)
            viewModel.searchManga("")
        }

        binding.etSearchBusqueda.doAfterTextChanged { text ->
            viewModel.onSearchQueryChanged(text?.toString() ?: "")
        }

        binding.etSearchBusqueda.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.searchManga()
                true
            } else {
                false
            }
        }

        if (viewModel.uiState.value.searchQuery.isNotBlank() || viewModel.uiState.value.selectedStatus != null || viewModel.uiState.value.selectedGenreTagId != null) {
            binding.etSearchBusqueda.setText(viewModel.uiState.value.searchQuery)
            viewModel.searchManga()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    searchAdapter.submitList(state.searchResults)
                    binding.progressBarBusqueda.visibility = if (state.isLoading) View.VISIBLE else View.GONE

                    // Mostrar o ocultar chip de tag activo
                    if (!state.selectedGenreTagName.isNullOrBlank()) {
                        binding.chipActiveTag.visibility = View.VISIBLE
                        binding.chipActiveTag.text = "Etiqueta: ${state.selectedGenreTagName}"
                        binding.chipActiveTag.setOnCloseIconClickListener {
                            viewModel.setFilters(state.selectedStatus, null, null)
                            viewModel.searchManga()
                        }
                    } else {
                        binding.chipActiveTag.visibility = View.GONE
                    }

                    if (!state.isLoading && state.searchResults.isEmpty()) {
                        binding.tvEmptyBusqueda.visibility = View.VISIBLE
                        binding.tvEmptyBusqueda.text = state.errorMessage ?: "No se encontraron mangas."
                    } else {
                        binding.tvEmptyBusqueda.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setupDropdown() {
        val statusAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, statusOptions)
        binding.autoCompleteStatusBusqueda.setAdapter(statusAdapter)
        binding.autoCompleteStatusBusqueda.setOnItemClickListener { _, _, position, _ ->
            val selected = statusOptions[position]
            val status = when (selected) {
                "En Emisión" -> "ongoing"
                "Completado" -> "completed"
                else -> null
            }
            viewModel.setFilters(status, viewModel.uiState.value.selectedGenreTagId, viewModel.uiState.value.selectedGenreTagName)
            viewModel.searchManga()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
