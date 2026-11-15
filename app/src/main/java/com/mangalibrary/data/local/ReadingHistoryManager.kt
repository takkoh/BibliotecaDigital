package com.mangalibrary.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.mangalibrary.data.model.ChapterItem
import com.mangalibrary.data.model.displayTitle

data class ReadingProgress(
    val mangaId: String,
    val chapterId: String,
    val chapterTitle: String,
    val timestamp: Long = System.currentTimeMillis()
)

class ReadingHistoryManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("manga_reading_history_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveLastReadChapter(mangaId: String, chapter: ChapterItem) {
        val chapterName = chapter.displayTitle()
        val progress = ReadingProgress(
            mangaId = mangaId,
            chapterId = chapter.id,
            chapterTitle = chapterName
        )
        val json = gson.toJson(progress)
        prefs.edit().putString("history_$mangaId", json).apply()
    }

    fun getLastReadChapter(mangaId: String): ReadingProgress? {
        val json = prefs.getString("history_$mangaId", null) ?: return null
        return try {
            val progress = gson.fromJson(json, ReadingProgress::class.java)
            // Sanitizar por si hay registros antiguos mal formateados
            var cleanTitle = progress.chapterTitle.replace("Capítulo -: ", "").replace("Capítulo - : ", "")
            if (cleanTitle.contains("Capítulo Capítulo")) {
                cleanTitle = cleanTitle.replace("Capítulo Capítulo", "Capítulo")
            }
            progress.copy(chapterTitle = cleanTitle)
        } catch (e: Exception) {
            null
        }
    }
}
