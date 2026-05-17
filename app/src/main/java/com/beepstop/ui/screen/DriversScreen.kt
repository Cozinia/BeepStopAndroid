package com.beepstop.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.activity.compose.BackHandler
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.beepstop.data.model.DriverMetadataStore
import com.beepstop.data.model.StandingsDriver
import com.beepstop.ui.components.EmptyState
import com.beepstop.ui.components.ErrorState
import com.beepstop.ui.components.SkeletonTeamRow
import com.beepstop.ui.viewmodel.DriverViewModel

private val teamColors = mapOf(
    "red_bull"      to Color(0xFF3671C6),
    "ferrari"       to Color(0xFFE8002D),
    "mercedes"      to Color(0xFF27F4D2),
    "mclaren"       to Color(0xFFFF8000),
    "aston_martin"  to Color(0xFF229971),
    "alpine"        to Color(0xFFFF87BC),
    "williams"      to Color(0xFF64C4FF),
    "haas"          to Color(0xFFB6BABD),
    "sauber"        to Color(0xFF535353),
    "audi"          to Color(0xFF535353),
    "cadillac"      to Color(0xFF860505),
    "rb"            to Color(0xFF6692FF),
    "racing_bulls"  to Color(0xFF6692FF),
)

internal fun driverTeamColor(constructorId: String): Color =
    teamColors[constructorId] ?: Color(0xFF888888)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriversScreen(
    viewModel: DriverViewModel,
    modifier: Modifier = Modifier
) {
    val drivers by viewModel.drivers.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val selectedDriver by viewModel.selectedDriver.collectAsState()

    BackHandler(enabled = selectedDriver != null) {
        viewModel.clearSelection()
    }

    if (selectedDriver != null) {
        DriverBioScreen(
            viewModel = viewModel,
            onBack = { viewModel.clearSelection() }
        )
        return
    }

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Drivers") })
        PullToRefreshBox(
            isRefreshing = isLoading,
            onRefresh = { viewModel.refresh() },
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                error != null && drivers.isEmpty() -> {
                    ErrorState(
                        message = "Could not load driver standings",
                        onRetry = { viewModel.refresh() }
                    )
                }
                isLoading && drivers.isEmpty() -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(10) { SkeletonTeamRow() }
                    }
                }
                drivers.isEmpty() -> {
                    EmptyState(
                        icon = Icons.Default.Person,
                        message = "Driver standings unavailable",
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
                        itemsIndexed(drivers, key = { _, d -> d.id }) { index, driver ->
                            DriverStandingRow(
                                position = index + 1,
                                driver = driver,
                                onClick = { viewModel.selectDriver(driver) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DriverStandingRow(
    position: Int,
    driver: StandingsDriver,
    onClick: () -> Unit
) {
    val teamColor = driverTeamColor(driver.constructorId)
    val meta = DriverMetadataStore.get(driver.id)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(teamColor)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$position",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = teamColor,
                    modifier = Modifier.width(28.dp)
                )
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(teamColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    var photoFailed by remember { mutableStateOf(false) }
                    if (meta != null && meta.photoUrl.isNotEmpty() && !photoFailed) {
                        AsyncImage(
                            model = meta.photoUrl,
                            contentDescription = driver.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            onError = { photoFailed = true }
                        )
                    } else {
                        Text(
                            text = driver.code.take(2),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = teamColor
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = driver.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = meta?.nationality ?: driver.constructorId,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${driver.points}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = teamColor
                    )
                    Text(
                        text = "pts",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
