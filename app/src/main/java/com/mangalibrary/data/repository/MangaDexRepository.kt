package com.mangalibrary.data.repository

import com.mangalibrary.data.api.MangaDexApi
import com.mangalibrary.data.model.AuthorItem
import com.mangalibrary.data.model.ChapterItem
import com.mangalibrary.data.model.MangaDataDto
import com.mangalibrary.data.model.MangaItem
import com.mangalibrary.data.model.TagItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MangaDexRepository(
    private val api: MangaDexApi = MangaDexApi.create()
) {

    suspend fun searchManga(
        query: String?,
        includedTags: List<String>? = null,
        status: List<String>? = null,
        limit: Int = 20,
        offset: Int = 0
    ): Result<List<MangaItem>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.searchManga(
                    title = query?.ifBlank { null },
                    includedTags = includedTags?.ifEmpty { null },
                    status = status?.ifEmpty { null },
                    limit = limit,
                    offset = offset
                )
                val items = response.data.map { dtoToMangaItem(it) }
                Result.success(items)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getMangaDetails(id: String): Result<MangaItem> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getMangaDetails(id)
                Result.success(dtoToMangaItem(response.data))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getMangaChapters(id: String, limit: Int = 50, offset: Int = 0): Result<List<ChapterItem>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getMangaChapters(id, limit = limit, offset = offset)
                val chapters = response.data.map { dto ->
                    val attr = dto.attributes
                    ChapterItem(
                        id = dto.id,
                        chapter = attr.chapter,
                        title = attr.title,
                        volume = attr.volume,
                        translatedLanguage = attr.translatedLanguage ?: "unknown",
                        publishAt = attr.publishAt,
                        externalUrl = attr.externalUrl
                    )
                }
                Result.success(chapters)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Obtiene las URLs completas de las páginas de un capítulo determinado
     * consultando el servidor At-Home de MangaDex.
     */
    suspend fun getChapterPages(chapterId: String): Result<List<String>> {
        return withContext(Dispatchers.IO) {
            try {
                val atHomeResponse = api.getAtHomeServer(chapterId)
                val baseUrl = atHomeResponse.baseUrl
                val hash = atHomeResponse.chapter.hash
                // Usamos 'data' para alta calidad o 'dataSaver' para menor consumo de datos
                val pageFiles = atHomeResponse.chapter.data

                // Construimos la URL completa para cada página: {baseUrl}/data/{hash}/{filename}
                val pageUrls = pageFiles.map { filename ->
                    "$baseUrl/data/$hash/$filename"
                }
                Result.success(pageUrls)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private fun dtoToMangaItem(dto: MangaDataDto): MangaItem {
        val attr = dto.attributes
        val title = attr.title?.get("es")
            ?: attr.title?.get("en")
            ?: attr.title?.values?.firstOrNull()
            ?: "Título desconocido"

        val description = attr.description?.get("es")
            ?: attr.description?.get("en")
            ?: attr.description?.values?.firstOrNull()
            ?: "Sin descripción disponible."

        val tags = attr.tags?.map { tagDto ->
            val tagTitle = tagDto.attributes.name?.get("en")
                ?: tagDto.attributes.name?.values?.firstOrNull()
                ?: "Sin etiqueta"
            TagItem(
                id = tagDto.id,
                name = tagTitle,
                group = tagDto.attributes.group ?: "tag"
            )
        } ?: emptyList()

        val genres = tags.filter { it.group.lowercase() == "genre" }

        // Find cover filename from relationships
        val coverFileName = dto.relationships
            ?.firstOrNull { it.type == "cover_art" }
            ?.attributes?.get("fileName") as? String

        val coverUrl = coverFileName?.let {
            "${MangaDexApi.COVER_BASE_URL}/${dto.id}/$it"
        }

        // Find authors from relationships
        val authors = dto.relationships
            ?.filter { it.type == "author" || it.type == "artist" }
            ?.mapNotNull { rel ->
                val authorName = rel.attributes?.get("name") as? String
                if (authorName != null) AuthorItem(id = rel.id, name = authorName) else null
            } ?: emptyList()

        return MangaItem(
            id = dto.id,
            title = title,
            description = description,
            coverUrl = coverUrl,
            genres = genres,
            tags = tags,
            authors = authors,
            status = attr.status ?: "desconocido",
            contentRating = attr.contentRating ?: "safe",
            year = attr.year
        )
    }
}
