package com.mangalibrary.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mangalibrary.data.model.MangaItem

class FavoritesManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("manga_favorites_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val key = "favorite_mangas_list"

    fun getFavorites(): List<MangaItem> {
        val json = prefs.getString(key, null) ?: return emptyList()
        val type = object : TypeToken<List<MangaItem>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun isFavorite(mangaId: String): Boolean {
        return getFavorites().any { it.id == mangaId }
    }

    fun toggleFavorite(manga: MangaItem): Boolean {
        val currentList = getFavorites().toMutableList()
        val index = currentList.indexOfFirst { it.id == manga.id }
        val isFavNow: Boolean
        if (index >= 0) {
            currentList.removeAt(index)
            isFavNow = false
        } else {
            currentList.add(0, manga)
            isFavNow = true
        }
        val json = gson.toJson(currentList)
        prefs.edit().putString(key, json).apply()
        return isFavNow
    }
}
