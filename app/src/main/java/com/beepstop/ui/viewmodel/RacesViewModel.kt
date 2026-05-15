package com.beepstop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.beepstop.data.model.Race
import com.beepstop.data.repository.RaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RacesViewModel(private val repository: RaceRepository) : ViewModel() {

    private val _races = MutableStateFlow<List<Race>>(emptyList())
    val races: StateFlow<List<Race>> = _races

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init { load() }

    fun refresh() = load(forceRefresh = true)

    private fun load(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            repository.getRaces(forceRefresh).collect { result ->
                result.fold(
                    onSuccess = { races ->
                        _races.value = races
                        _isLoading.value = false
                    },
                    onFailure = { e ->
                        _error.value = e.message ?: "Failed to load races"
                        _isLoading.value = false
                    }
                )
            }
        }
    }

    companion object {
        fun factory(repository: RaceRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>) =
                RacesViewModel(repository) as T
        }
    }
}
