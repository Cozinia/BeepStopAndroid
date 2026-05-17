package com.beepstop.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.beepstop.data.model.DriverMetadataStore
import com.beepstop.data.model.StandingsDriver
import com.beepstop.data.remote.WikipediaClient
import com.beepstop.data.repository.StandingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DriverViewModel(private val repository: StandingsRepository) : ViewModel() {

    private val _drivers = MutableStateFlow<List<StandingsDriver>>(emptyList())
    val drivers: StateFlow<List<StandingsDriver>> = _drivers

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _selectedDriver = MutableStateFlow<StandingsDriver?>(null)
    val selectedDriver: StateFlow<StandingsDriver?> = _selectedDriver

    private val _wikiExtract = MutableStateFlow<String?>(null)
    val wikiExtract: StateFlow<String?> = _wikiExtract

    private val _wikiLoading = MutableStateFlow(false)
    val wikiLoading: StateFlow<Boolean> = _wikiLoading

    private val wikiCache = mutableMapOf<String, String>()

    init { load() }

    fun refresh() = load(forceRefresh = true)

    private fun load(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            repository.getDriverStandings(forceRefresh).collect { result ->
                result.fold(
                    onSuccess = { drivers ->
                        _drivers.value = drivers
                        _isLoading.value = false
                    },
                    onFailure = { e ->
                        _error.value = e.message ?: "Failed to load driver standings"
                        _isLoading.value = false
                    }
                )
            }
        }
    }

    fun selectDriver(driver: StandingsDriver) {
        _selectedDriver.value = driver
        loadWikiBio(driver.id)
    }

    fun clearSelection() {
        _selectedDriver.value = null
        _wikiExtract.value = null
    }

    private fun loadWikiBio(driverId: String) {
        val cached = wikiCache[driverId]
        if (cached != null) {
            _wikiExtract.value = cached
            return
        }
        viewModelScope.launch {
            _wikiLoading.value = true
            _wikiExtract.value = null
            try {
                val metadata = DriverMetadataStore.get(driverId)
                val slug = metadata?.wikipediaSlug ?: driverId.replace("_", " ")
                    .split(" ").joinToString("_") { it.replaceFirstChar(Char::uppercase) }
                Log.d("DriverViewModel", "Fetching Wikipedia: $slug")
                val summary = WikipediaClient.api.getSummary(slug)
                val extract = summary.extract
                wikiCache[driverId] = extract
                _wikiExtract.value = extract
            } catch (e: Exception) {
                Log.e("DriverViewModel", "Wikipedia fetch failed for $driverId", e)
                _wikiExtract.value = null
            } finally {
                _wikiLoading.value = false
            }
        }
    }

    companion object {
        fun factory(repository: StandingsRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>) =
                DriverViewModel(repository) as T
        }
    }
}
