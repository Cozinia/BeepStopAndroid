package com.beepstop.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "widget_prefs")

class PreferencesManager(private val context: Context) {

    companion object {
        val KEY_BG_COLOR = stringPreferencesKey("bg_color")
        val KEY_FONT_COLOR = stringPreferencesKey("font_color")
        val KEY_CIRCUIT_COLOR = stringPreferencesKey("circuit_color")

        val KEY_STANDINGS_SMALL_MODE = stringPreferencesKey("standings_small_mode")
        val KEY_STANDINGS_SMALL_ID = stringPreferencesKey("standings_small_id")
        val KEY_STANDINGS_LARGE_MODE = stringPreferencesKey("standings_large_mode")
        val KEY_STANDINGS_LARGE_IDS = stringPreferencesKey("standings_large_ids")

        val KEY_COUNTDOWN_TARGET = stringPreferencesKey("countdown_target")
        val KEY_COUNTDOWN_BG_COLOR = stringPreferencesKey("countdown_bg_color")
        val KEY_COUNTDOWN_FONT_COLOR = stringPreferencesKey("countdown_font_color")

        val KEY_NEXT_SESSION_DATE = longPreferencesKey("next_session_date")
        val KEY_NEXT_SESSION_LABEL = stringPreferencesKey("next_session_label")
        val KEY_NEXT_SESSION_RACE = stringPreferencesKey("next_session_race")
        val KEY_NEXT_CIRCUIT_ID   = stringPreferencesKey("next_circuit_id")
        val KEY_NEXT_CIRCUIT_NAME = stringPreferencesKey("next_circuit_name")
        val KEY_LAST_EXPIRED_DATE = longPreferencesKey("last_expired_session_date")
        val KEY_LAST_STANDINGS_RELOAD = longPreferencesKey("last_standings_reload")

        val KEY_STANDINGS_CACHE = stringPreferencesKey("standings_cache_json")
    }

    fun <T> observe(key: androidx.datastore.preferences.core.Preferences.Key<T>, default: T): Flow<T> =
        context.dataStore.data.map { it[key] ?: default }

    suspend fun <T> save(key: androidx.datastore.preferences.core.Preferences.Key<T>, value: T) {
        context.dataStore.edit { it[key] = value }
    }
}
