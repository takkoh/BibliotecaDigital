package com.mangalibrary.Fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.mangalibrary.R
import com.mangalibrary.data.local.FavoritesManager
import com.mangalibrary.databinding.FragmentFavoritesBinding
import com.mangalibrary.ui.MangaViewModel
import com.mangalibrary.ui.adapters.MangaCarouselAdapter

class FragmentMisFavoritos : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MangaViewModel by activityViewModels()
    private lateinit var favoritesManager: FavoritesManager
    private lateinit var adapter: MangaCarouselAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesManager = FavoritesManager(requireContext())

        adapter = MangaCarouselAdapter { manga ->
            viewModel.selectManga(manga)
            parentFragmentManager.beginTransaction()
                .replace(R.id.FragmentL1, FragmentDetalleManga())
                .addToBackStack(null)
                .commit()
        }

        binding.rvFavorites.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.rvFavorites.adapter = adapter

        loadFavorites()
    }

    override fun onResume() {
        super.onResume()
        loadFavorites()
    }

    private fun loadFavorites() {
        val favorites = favoritesManager.getFavorites()
        adapter.submitList(favorites)

        if (favorites.isEmpty()) {
            binding.tvEmptyFavorites.visibility = View.VISIBLE
            binding.rvFavorites.visibility = View.GONE
        } else {
            binding.tvEmptyFavorites.visibility = View.GONE
            binding.rvFavorites.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
