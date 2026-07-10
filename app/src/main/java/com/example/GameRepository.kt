package com.example

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "lightflow_settings")

class GameRepository(private val context: Context) {
    companion object {
        private val UNLOCKED_LEVEL_KEY = intPreferencesKey("unlocked_level")
        private val EXTRA_MOVES_ITEM_KEY = intPreferencesKey("extra_moves_item")
        private val BYPASS_ITEM_KEY = intPreferencesKey("bypass_item")
    }

    val unlockedLevel: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[UNLOCKED_LEVEL_KEY] ?: 1
    }

    val extraMovesItemCount: Flow<Int> = context.dataStore.data.map { it[EXTRA_MOVES_ITEM_KEY] ?: 3 }
    val bypassItemCount: Flow<Int> = context.dataStore.data.map { it[BYPASS_ITEM_KEY] ?: 2 }

    val levelScores: Flow<Map<Int, Int>> = context.dataStore.data.map { prefs ->
        val scores = mutableMapOf<Int, Int>()
        prefs.asMap().forEach { (key, value) ->
            if (key.name.startsWith("level_score_") && value is Int) {
                val level = key.name.removePrefix("level_score_").toIntOrNull()
                if (level != null) {
                    scores[level] = value
                }
            }
        }
        scores
    }

    suspend fun saveLevelScore(level: Int, score: Int) {
        context.dataStore.edit { prefs ->
            val key = intPreferencesKey("level_score_$level")
            val current = prefs[key] ?: 0
            if (score > current) {
                prefs[key] = score
            }
        }
    }

    suspend fun unlockLevel(level: Int) {
        context.dataStore.edit { preferences ->
            val current = preferences[UNLOCKED_LEVEL_KEY] ?: 1
            if (level > current) {
                preferences[UNLOCKED_LEVEL_KEY] = level
                val currentMoves = preferences[EXTRA_MOVES_ITEM_KEY] ?: 3
                val currentBypass = preferences[BYPASS_ITEM_KEY] ?: 2
                // Reward 1 Overclock item for clearing
                preferences[EXTRA_MOVES_ITEM_KEY] = currentMoves + 1
                // Reward 1 Bypass item every 2 levels
                if (level % 2 == 0) {
                    preferences[BYPASS_ITEM_KEY] = currentBypass + 1
                }
            }
        }
    }

    suspend fun useExtraMovesItem(): Boolean {
        var success = false
        context.dataStore.edit { preferences ->
            val current = preferences[EXTRA_MOVES_ITEM_KEY] ?: 0
            if (current > 0) {
                preferences[EXTRA_MOVES_ITEM_KEY] = current - 1
                success = true
            }
        }
        return success
    }

    suspend fun useBypassItem(): Boolean {
        var success = false
        context.dataStore.edit { preferences ->
            val current = preferences[BYPASS_ITEM_KEY] ?: 0
            if (current > 0) {
                preferences[BYPASS_ITEM_KEY] = current - 1
                success = true
            }
        }
        return success
    }
}
