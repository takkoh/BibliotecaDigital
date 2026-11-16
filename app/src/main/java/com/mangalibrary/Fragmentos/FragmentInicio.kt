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
import com.mangalibrary.databinding.FragmentMangaSearchBinding
import com.mangalibrary.ui.MangaViewModel
import com.mangalibrary.ui.adapters.MangaCarouselAdapter
import kotlinx.coroutines.launch

class FragmentInicio : Fragment() {

    private var _binding: FragmentMangaSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MangaViewModel by activityViewModels()
    private lateinit var trendingAdapter: MangaCarouselAdapter
    private lateinit var recentAdapter: MangaCarouselAdapter

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

        val onMangaClick: (com.mangalibrary.data.model.MangaItem) -> Unit = { selectedManga ->
            viewModel.selectManga(selectedManga)
            parentFragmentManager.beginTransaction()
                .replace(R.id.FragmentL1, FragmentDetalleManga())
                .addToBackStack(null)
                .commit()
        }

        trendingAdapter = MangaCarouselAdapter(onMangaClick)
        recentAdapter = MangaCarouselAdapter(onMangaClick)

        binding.rvTrending.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvTrending.adapter = trendingAdapter

        binding.rvRecent.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecent.adapter = recentAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    trendingAdapter.submitList(state.trendingMangas)
                    recentAdapter.submitList(state.recentlyUpdatedMangas)

                    binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
