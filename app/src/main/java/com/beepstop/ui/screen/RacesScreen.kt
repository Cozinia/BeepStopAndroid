package com.beepstop.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import com.beepstop.ui.components.EmptyState
import com.beepstop.ui.components.ErrorState
import com.beepstop.ui.components.RaceRowItem
import com.beepstop.ui.components.SkeletonRaceRow
import com.beepstop.ui.viewmodel.RacesViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RacesScreen(
    viewModel: RacesViewModel,
    modifier: Modifier = Modifier
) {
    val races by viewModel.races.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val today = LocalDate.now()
    val upcomingRaces = races.filter { race ->
        try { LocalDate.parse(race.date).isAfter(today) } catch (e: Exception) { false }
    }

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(title = { Text("2026 Season") })
        PullToRefreshBox(
            isRefreshing = isLoading,
            onRefresh = { viewModel.refresh() },
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                error != null && upcomingRaces.isEmpty() -> {
                    ErrorState(
                        message = "Could not load schedule",
                        onRetry = { viewModel.refresh() }
                    )
                }
                isLoading && upcomingRaces.isEmpty() -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(5) { SkeletonRaceRow() }
                    }
                }
                upcomingRaces.isEmpty() -> {
                    EmptyState(
                        icon = Icons.Default.CalendarMonth,
                        message = "No upcoming races scheduled",
                        action = { viewModel.refresh() },
                        actionLabel = "Retry"
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(upcomingRaces) { index, race ->
                            RaceRowItem(race = race, isNext = index == 0)
                        }
                    }
                }
            }
        }
    }
}
