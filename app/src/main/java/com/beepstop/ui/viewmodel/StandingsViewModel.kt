package com.beepstop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.beepstop.data.model.StandingsTeam
import com.beepstop.data.repository.StandingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StandingsViewModel(private val repository: StandingsRepository) : ViewModel() {

    private val _teams = MutableStateFlow<List<StandingsTeam>>(emptyList())
    val teams: StateFlow<List<StandingsTeam>> = _teams

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _expandedTeamId = MutableStateFlow<String?>(null)
    val expandedTeamId: StateFlow<String?> = _expandedTeamId

    init { load() }

    fun toggleTeam(id: String) {
        _expandedTeamId.value = if (_expandedTeamId.value == id) null else id
    }

    fun refresh() = load(forceRefresh = true)

    private fun load(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            repository.getConstructorStandings(forceRefresh).collect { result ->
                result.fold(
                    onSuccess = { teams ->
                        _teams.value = teams
                        _isLoading.value = false
                    },
                    onFailure = { e ->
                        _error.value = e.message ?: "Failed to load standings"
                        _isLoading.value = false
                    }
                )
            }
        }
    }

    companion object {
        fun factory(repository: StandingsRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>) =
                StandingsViewModel(repository) as T
        }
    }
}
