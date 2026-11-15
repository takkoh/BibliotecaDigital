package com.mangalibrary.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.mangalibrary.data.model.ChapterItem

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
        val chapterName = "Capítulo ${chapter.chapter ?: "-"}${chapter.title?.let { ": $it" } ?: ""}"
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
            gson.fromJson(json, ReadingProgress::class.java)
        } catch (e: Exception) {
            null
        }
    }
}
