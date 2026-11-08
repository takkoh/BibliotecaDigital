package com.mangalibrary.data.model

import com.google.gson.annotations.SerializedName

// --- Domain Models ---

data class TagItem(
    val id: String,
    val name: String,
    val group: String
)

data class AuthorItem(
    val id: String,
    val name: String
)

data class MangaItem(
    val id: String,
    val title: String,
    val description: String,
    val coverUrl: String?,
    val genres: List<TagItem>,
    val tags: List<TagItem>,
    val authors: List<AuthorItem>,
    val status: String,
    val contentRating: String,
    val year: Int?
)

data class ChapterItem(
    val id: String,
    val chapter: String?,
    val title: String?,
    val volume: String?,
    val translatedLanguage: String,
    val publishAt: String?,
    val externalUrl: String?
)

data class RecommendationResult(
    val manga: MangaItem,
    val score: Int,
    val matchingGenres: List<String>,
    val matchingTags: List<String>,
    val sameAuthor: Boolean
)

// --- MangaDex API DTO Models ---

data class MangaDexListResponse<T>(
    val result: String,
    val response: String,
    val data: List<T>,
    val limit: Int,
    val offset: Int,
    val total: Int
)

data class MangaDexSingleResponse<T>(
    val result: String,
    val response: String,
    val data: T
)

data class MangaDataDto(
    val id: String,
    val type: String,
    val attributes: MangaAttributesDto,
    val relationships: List<RelationshipDto>? = emptyList()
)

data class MangaAttributesDto(
    val title: Map<String, String>?,
    val altTitles: List<Map<String, String>>?,
    val description: Map<String, String>?,
    val status: String?,
    val year: Int?,
    val contentRating: String?,
    val tags: List<TagDto>?
)

data class TagDto(
    val id: String,
    val type: String,
    val attributes: TagAttributesDto
)

data class TagAttributesDto(
    val name: Map<String, String>?,
    val group: String?
)

data class RelationshipDto(
    val id: String,
    val type: String,
    val attributes: Map<String, Any>?
)

data class ChapterDataDto(
    val id: String,
    val type: String,
    val attributes: ChapterAttributesDto,
    val relationships: List<RelationshipDto>? = emptyList()
)

data class ChapterAttributesDto(
    val volume: String?,
    val chapter: String?,
    val title: String?,
    val translatedLanguage: String?,
    val publishAt: String?,
    val externalUrl: String?
)

data class CoverDataDto(
    val id: String,
    val type: String,
    val attributes: CoverAttributesDto
)

data class CoverAttributesDto(
    val fileName: String?
)

// --- At-Home API DTO Models (Para obtener las páginas de lectura) ---

data class AtHomeResponseDto(
    val result: String,
    val baseUrl: String,
    val chapter: AtHomeChapterDto
)

data class AtHomeChapterDto(
    val hash: String,
    val data: List<String>,       // Lista de nombres de archivos de alta calidad
    val dataSaver: List<String>   // Lista de nombres de archivos comprimidos (ahorro de datos)
)
