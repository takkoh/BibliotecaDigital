package com.mangalibrary.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mangalibrary.data.model.ChapterItem
import com.mangalibrary.data.model.MangaItem
import com.mangalibrary.data.model.RecommendationResult
import com.mangalibrary.data.repository.MangaDexRepository
import com.mangalibrary.domain.RecommendationEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MangaUiState(
    val searchQuery: String = "",
    val searchResults: List<MangaItem> = emptyList(),
    val selectedManga: MangaItem? = null,
    val chapters: List<ChapterItem> = emptyList(),
    val recommendations: List<RecommendationResult> = emptyList(),
    val isLoading: Boolean = false,
    val isDetailLoading: Boolean = false,
    val errorMessage: String? = null
)

class MangaViewModel(
    private val repository: MangaDexRepository = MangaDexRepository(),
    private val recommendationEngine: RecommendationEngine = RecommendationEngine()
) : ViewModel() {

    private val _uiState = MutableStateFlow(MangaUiState())
    val uiState: StateFlow<MangaUiState> = _uiState.asStateFlow()

    init {
        // Initial popular / default search
        searchManga("")
    }

    fun onSearchQueryChanged(newQuery: String) {
        _uiState.value = _uiState.value.copy(searchQuery = newQuery)
    }

    fun searchManga(query: String = _uiState.value.searchQuery) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.searchManga(query)
            result.onSuccess { mangas ->
                _uiState.value = _uiState.value.copy(
                    searchResults = mangas,
                    isLoading = false
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.localizedMessage ?: "Error al buscar mangas en MangaDex"
                )
            }
        }
    }

    fun selectManga(manga: MangaItem) {
        _uiState.value = _uiState.value.copy(
            selectedManga = manga,
            isDetailLoading = true,
            chapters = emptyList(),
            recommendations = emptyList()
        )

        viewModelScope.launch {
            // Load Chapters
            val chapterResult = repository.getMangaChapters(manga.id)
            val loadedChapters = chapterResult.getOrDefault(emptyList())

            // Generate Recommendations based on search candidates
            val candidates = _uiState.value.searchResults
            val recs = recommendationEngine.getRecommendations(manga, candidates)

            _uiState.value = _uiState.value.copy(
                chapters = loadedChapters,
                recommendations = recs,
                isDetailLoading = false
            )
        }
    }

    fun clearSelectedManga() {
        _uiState.value = _uiState.value.copy(
            selectedManga = null,
            chapters = emptyList(),
            recommendations = emptyList()
        )
    }
}
