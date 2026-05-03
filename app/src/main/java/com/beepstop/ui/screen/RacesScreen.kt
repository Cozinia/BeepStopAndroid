package com.beepstop.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.beepstop.ui.components.RaceRowItem
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
                isLoading && upcomingRaces.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                upcomingRaces.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No upcoming races")
                    }
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
