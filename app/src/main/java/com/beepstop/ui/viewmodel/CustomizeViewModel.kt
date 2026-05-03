package com.beepstop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.beepstop.data.local.PreferencesManager
import com.beepstop.data.local.PreferencesManager.Companion.KEY_BG_COLOR
import com.beepstop.data.local.PreferencesManager.Companion.KEY_CIRCUIT_COLOR
import com.beepstop.data.local.PreferencesManager.Companion.KEY_COUNTDOWN_BG_COLOR
import com.beepstop.data.local.PreferencesManager.Companion.KEY_COUNTDOWN_FONT_COLOR
import com.beepstop.data.local.PreferencesManager.Companion.KEY_COUNTDOWN_TARGET
import com.beepstop.data.local.PreferencesManager.Companion.KEY_FONT_COLOR
import com.beepstop.data.local.PreferencesManager.Companion.KEY_STANDINGS_LARGE_IDS
import com.beepstop.data.local.PreferencesManager.Companion.KEY_STANDINGS_LARGE_MODE
import com.beepstop.data.local.PreferencesManager.Companion.KEY_STANDINGS_SMALL_ID
import com.beepstop.data.local.PreferencesManager.Companion.KEY_STANDINGS_SMALL_MODE
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CustomizeViewModel(private val prefs: PreferencesManager) : ViewModel() {

    val bgColor: StateFlow<String> = prefs.observe(KEY_BG_COLOR, "#1C1C1E")
        .stateIn(viewModelScope, SharingStarted.Eagerly, "#1C1C1E")

    val fontColor: StateFlow<String> = prefs.observe(KEY_FONT_COLOR, "#FFFFFF")
        .stateIn(viewModelScope, SharingStarted.Eagerly, "#FFFFFF")

    val circuitColor: StateFlow<String> = prefs.observe(KEY_CIRCUIT_COLOR, "#DC0000")
        .stateIn(viewModelScope, SharingStarted.Eagerly, "#DC0000")

    val standingsSmallMode: StateFlow<String> = prefs.observe(KEY_STANDINGS_SMALL_MODE, "team")
        .stateIn(viewModelScope, SharingStarted.Eagerly, "team")

    val standingsSmallId: StateFlow<String> = prefs.observe(KEY_STANDINGS_SMALL_ID, "")
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")

    val standingsLargeMode: StateFlow<String> = prefs.observe(KEY_STANDINGS_LARGE_MODE, "driver")
        .stateIn(viewModelScope, SharingStarted.Eagerly, "driver")

    val standingsLargeIds: StateFlow<String> = prefs.observe(KEY_STANDINGS_LARGE_IDS, "[]")
        .stateIn(viewModelScope, SharingStarted.Eagerly, "[]")

    val countdownTarget: StateFlow<String> = prefs.observe(KEY_COUNTDOWN_TARGET, "race")
        .stateIn(viewModelScope, SharingStarted.Eagerly, "race")

    val countdownBgColor: StateFlow<String> = prefs.observe(KEY_COUNTDOWN_BG_COLOR, "#1C1C1E")
        .stateIn(viewModelScope, SharingStarted.Eagerly, "#1C1C1E")

    val countdownFontColor: StateFlow<String> = prefs.observe(KEY_COUNTDOWN_FONT_COLOR, "#FFFFFF")
        .stateIn(viewModelScope, SharingStarted.Eagerly, "#FFFFFF")

    fun applySettings(
        bgColor: String? = null,
        fontColor: String? = null,
        circuitColor: String? = null,
        standingsSmallMode: String? = null,
        standingsSmallId: String? = null,
        standingsLargeMode: String? = null,
        standingsLargeIds: String? = null,
        countdownTarget: String? = null,
        countdownBgColor: String? = null,
        countdownFontColor: String? = null,
    ) {
        viewModelScope.launch {
            bgColor?.let { prefs.save(KEY_BG_COLOR, it) }
            fontColor?.let { prefs.save(KEY_FONT_COLOR, it) }
            circuitColor?.let { prefs.save(KEY_CIRCUIT_COLOR, it) }
            standingsSmallMode?.let { prefs.save(KEY_STANDINGS_SMALL_MODE, it) }
            standingsSmallId?.let { prefs.save(KEY_STANDINGS_SMALL_ID, it) }
            standingsLargeMode?.let { prefs.save(KEY_STANDINGS_LARGE_MODE, it) }
            standingsLargeIds?.let { prefs.save(KEY_STANDINGS_LARGE_IDS, it) }
            countdownTarget?.let { prefs.save(KEY_COUNTDOWN_TARGET, it) }
            countdownBgColor?.let { prefs.save(KEY_COUNTDOWN_BG_COLOR, it) }
            countdownFontColor?.let { prefs.save(KEY_COUNTDOWN_FONT_COLOR, it) }
            // Widget refresh will be wired in tasks 17–19
        }
    }

    companion object {
        fun factory(prefs: PreferencesManager) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>) =
                CustomizeViewModel(prefs) as T
        }
    }
}
