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
import com.beepstop.data.local.PreferencesManager.Companion.KEY_NEXT_CIRCUIT_ID
import com.beepstop.data.local.PreferencesManager.Companion.KEY_STANDINGS_LARGE_IDS
import com.beepstop.data.local.PreferencesManager.Companion.KEY_STANDINGS_LARGE_MODE
import com.beepstop.data.local.PreferencesManager.Companion.KEY_STANDINGS_SMALL_ID
import com.beepstop.data.local.PreferencesManager.Companion.KEY_STANDINGS_SMALL_MODE
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CustomizeViewModel(private val prefs: PreferencesManager) : ViewModel() {

    val bgColor: StateFlow<String> = prefs.observe(KEY_BG_COLOR, "1C1C1E")
        .stateIn(viewModelScope, SharingStarted.Eagerly, "1C1C1E")

    val fontColor: StateFlow<String> = prefs.observe(KEY_FONT_COLOR, "FFFFFF")
        .stateIn(viewModelScope, SharingStarted.Eagerly, "FFFFFF")

    val circuitColor: StateFlow<String> = prefs.observe(KEY_CIRCUIT_COLOR, "DC0000")
        .stateIn(viewModelScope, SharingStarted.Eagerly, "DC0000")

    val standingsSmallMode: StateFlow<String> = prefs.observe(KEY_STANDINGS_SMALL_MODE, "team")
        .stateIn(viewModelScope, SharingStarted.Eagerly, "team")

    val standingsSmallId: StateFlow<String> = prefs.observe(KEY_STANDINGS_SMALL_ID, "mercedes")
        .stateIn(viewModelScope, SharingStarted.Eagerly, "mercedes")

    val standingsLargeMode: StateFlow<String> = prefs.observe(KEY_STANDINGS_LARGE_MODE, "driver")
        .stateIn(viewModelScope, SharingStarted.Eagerly, "driver")

    val standingsLargeIds: StateFlow<String> = prefs.observe(KEY_STANDINGS_LARGE_IDS, "[]")
        .stateIn(viewModelScope, SharingStarted.Eagerly, "[]")

    val countdownTarget: StateFlow<String> = prefs.observe(KEY_COUNTDOWN_TARGET, "race")
        .stateIn(viewModelScope, SharingStarted.Eagerly, "race")

    val countdownBgColor: StateFlow<String> = prefs.observe(KEY_COUNTDOWN_BG_COLOR, "1C1C1E")
        .stateIn(viewModelScope, SharingStarted.Eagerly, "1C1C1E")

    val countdownFontColor: StateFlow<String> = prefs.observe(KEY_COUNTDOWN_FONT_COLOR, "FFFFFF")
        .stateIn(viewModelScope, SharingStarted.Eagerly, "FFFFFF")

    val nextCircuitId: StateFlow<String> = prefs.observe(KEY_NEXT_CIRCUIT_ID, "bahrain")
        .stateIn(viewModelScope, SharingStarted.Eagerly, "bahrain")

    // Mutable local state for pending edits (not yet saved to DataStore)
    private val _pendingBgColor = MutableStateFlow(bgColor.value)
    val pendingBgColor: StateFlow<String> = _pendingBgColor

    private val _pendingFontColor = MutableStateFlow(fontColor.value)
    val pendingFontColor: StateFlow<String> = _pendingFontColor

    private val _pendingCircuitColor = MutableStateFlow(circuitColor.value)
    val pendingCircuitColor: StateFlow<String> = _pendingCircuitColor

    private val _pendingStandingsSmallMode = MutableStateFlow(standingsSmallMode.value)
    val pendingStandingsSmallMode: StateFlow<String> = _pendingStandingsSmallMode

    private val _pendingStandingsSmallId = MutableStateFlow(standingsSmallId.value)
    val pendingStandingsSmallId: StateFlow<String> = _pendingStandingsSmallId

    private val _pendingStandingsLargeMode = MutableStateFlow(standingsLargeMode.value)
    val pendingStandingsLargeMode: StateFlow<String> = _pendingStandingsLargeMode

    private val _pendingStandingsLargeIds = MutableStateFlow(standingsLargeIds.value)
    val pendingStandingsLargeIds: StateFlow<String> = _pendingStandingsLargeIds

    private val _pendingCountdownTarget = MutableStateFlow(countdownTarget.value)
    val pendingCountdownTarget: StateFlow<String> = _pendingCountdownTarget

    private val _pendingCountdownBgColor = MutableStateFlow(countdownBgColor.value)
    val pendingCountdownBgColor: StateFlow<String> = _pendingCountdownBgColor

    private val _pendingCountdownFontColor = MutableStateFlow(countdownFontColor.value)
    val pendingCountdownFontColor: StateFlow<String> = _pendingCountdownFontColor

    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent: SharedFlow<String> = _snackbarEvent

    fun setBgColor(hex: String) { _pendingBgColor.value = hex }
    fun setFontColor(hex: String) { _pendingFontColor.value = hex }
    fun setCircuitColor(hex: String) { _pendingCircuitColor.value = hex }
    fun setStandingsSmallMode(mode: String) { _pendingStandingsSmallMode.value = mode }
    fun setStandingsSmallId(id: String) { _pendingStandingsSmallId.value = id }
    fun setStandingsLargeMode(mode: String) { _pendingStandingsLargeMode.value = mode }
    fun setStandingsLargeIds(ids: String) { _pendingStandingsLargeIds.value = ids }
    fun setCountdownTarget(target: String) { _pendingCountdownTarget.value = target }
    fun setCountdownBgColor(hex: String) { _pendingCountdownBgColor.value = hex }
    fun setCountdownFontColor(hex: String) { _pendingCountdownFontColor.value = hex }

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
        }
    }

    fun saveAll() {
        viewModelScope.launch {
            prefs.save(KEY_BG_COLOR, _pendingBgColor.value)
            prefs.save(KEY_FONT_COLOR, _pendingFontColor.value)
            prefs.save(KEY_CIRCUIT_COLOR, _pendingCircuitColor.value)
            prefs.save(KEY_STANDINGS_SMALL_MODE, _pendingStandingsSmallMode.value)
            prefs.save(KEY_STANDINGS_SMALL_ID, _pendingStandingsSmallId.value)
            prefs.save(KEY_STANDINGS_LARGE_MODE, _pendingStandingsLargeMode.value)
            prefs.save(KEY_STANDINGS_LARGE_IDS, _pendingStandingsLargeIds.value)
            prefs.save(KEY_COUNTDOWN_TARGET, _pendingCountdownTarget.value)
            prefs.save(KEY_COUNTDOWN_BG_COLOR, _pendingCountdownBgColor.value)
            prefs.save(KEY_COUNTDOWN_FONT_COLOR, _pendingCountdownFontColor.value)
            _snackbarEvent.emit("Widget updated!")
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
