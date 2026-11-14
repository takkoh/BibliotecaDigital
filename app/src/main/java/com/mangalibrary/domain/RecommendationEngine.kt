package com.mangalibrary.domain

import com.mangalibrary.data.model.MangaItem
import com.mangalibrary.data.model.RecommendationResult

class RecommendationEngine {

    /**
     * Recomienda mangas basados en la regla especificada en especificaciones.pdf:
     * - Géneros coincidentes: +4 puntos cada uno
     * - Etiquetas coincidentes: +3 puntos cada una
     * - Mismo autor: +4 puntos
     * - Características similares (estado/rating): +1 punto cada una
     */
    fun getRecommendations(
        targetManga: MangaItem,
        candidates: List<MangaItem>,
        limit: Int = 10
    ): List<RecommendationResult> {
        val targetGenreIds = targetManga.genres.map { it.id }.toSet()
        val targetTagIds = targetManga.tags.map { it.id }.toSet()
        val targetAuthorNames = targetManga.authors.map { it.name.lowercase() }.toSet()

        return candidates
            .filter { candidate -> candidate.id != targetManga.id }
            .map { candidate ->
                var score = 0
                val matchingGenres = mutableListOf<String>()
                val matchingTags = mutableListOf<String>()
                var sameAuthor = false

                // 1. Géneros coincidentes (+4 pts c/u)
                candidate.genres.forEach { genre ->
                    if (targetGenreIds.contains(genre.id)) {
                        score += 4
                        matchingGenres.add(genre.name)
                    }
                }

                // 2. Etiquetas coincidentes (+3 pts c/u)
                candidate.tags.forEach { tag ->
                    if (targetTagIds.contains(tag.id)) {
                        score += 3
                        matchingTags.add(tag.name)
                    }
                }

                // 3. Mismo autor (+4 pts)
                val candidateAuthorNames = candidate.authors.map { it.name.lowercase() }
                if (candidateAuthorNames.any { targetAuthorNames.contains(it) }) {
                    score += 4
                    sameAuthor = true
                }

                // 4. Características similares (+1 pt estado, +1 pt contentRating)
                if (candidate.status.equals(targetManga.status, ignoreCase = true)) {
                    score += 1
                }
                if (candidate.contentRating.equals(targetManga.contentRating, ignoreCase = true)) {
                    score += 1
                }

                RecommendationResult(
                    manga = candidate,
                    score = score,
                    matchingGenres = matchingGenres,
                    matchingTags = matchingTags,
                    sameAuthor = sameAuthor
                )
            }
            .filter { it.score > 0 }
            .sortedByDescending { it.score }
            .take(limit)
    }
}
