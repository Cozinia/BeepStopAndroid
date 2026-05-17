package com.beepstop.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.beepstop.data.model.StandingsDriver
import com.beepstop.data.model.StandingsTeam
import com.beepstop.ui.components.EmptyState
import com.beepstop.ui.components.ErrorState
import com.beepstop.ui.components.SkeletonTeamRow
import com.beepstop.ui.viewmodel.StandingsViewModel

private val teamColors = mapOf(
    "red_bull" to Color(0xFF3671C6),
    "ferrari" to Color(0xFFE8002D),
    "mercedes" to Color(0xFF27F4D2),
    "mclaren" to Color(0xFFFF8000),
    "aston_martin" to Color(0xFF229971),
    "alpine" to Color(0xFFFF87BC),
    "williams" to Color(0xFF64C4FF),
    "haas" to Color(0xFFB6BABD),
    "sauber" to Color(0xFF52E252),
    "audi" to Color(0xFF535353),
    "cadillac" to Color(0xFF860505),
    "rb" to Color(0xFF6692FF),
)


private fun teamLogoUrl(constructorId: String): String {
    val newCdnBase = "https://media.formula1.com/image/upload/f_auto,c_limit,q_auto,w_180"
    return when (constructorId) {
        "red_bull" -> "$newCdnBase/common/f1/2026/redbullracing/2026redbullracinglogowhite.webp"
        "aston_martin" -> "$newCdnBase/common/f1/2026/astonmartin/2026astonmartinlogowhite.webp"
        "audi" -> "$newCdnBase/common/f1/2026/audi/2026audilogowhite.webp"
        "cadillac" -> "$newCdnBase/common/f1/2026/cadillac/2026cadillaclogowhite.webp"
        else -> {
            val slug = if (constructorId == "racing_bulls") "rb" else constructorId
            "$newCdnBase/content/dam/fom-website/2018-redesign-assets/team%20logos/$slug"
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandingsScreen(
    viewModel: StandingsViewModel,
    modifier: Modifier = Modifier
) {
    val teams by viewModel.teams.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val expandedTeamId by viewModel.expandedTeamId.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Championship") })
        PullToRefreshBox(
            isRefreshing = isLoading,
            onRefresh = { viewModel.refresh() },
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                error != null && teams.isEmpty() -> {
                    ErrorState(
                        message = "Could not load standings",
                        onRetry = { viewModel.refresh() }
                    )
                }
                isLoading && teams.isEmpty() -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(5) { SkeletonTeamRow() }
                    }
                }
                teams.isEmpty() -> {
                    EmptyState(
                        icon = Icons.Default.EmojiEvents,
                        message = "Standings unavailable",
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
                        items(teams, key = { it.id }) { team ->
                            TeamSection(
                                team = team,
                                expanded = expandedTeamId == team.id,
                                onToggle = { viewModel.toggleTeam(team.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TeamSection(
    team: StandingsTeam,
    expanded: Boolean,
    onToggle: () -> Unit
) {
    val teamColor = teamColors[team.id] ?: MaterialTheme.colorScheme.primary
    val chevronRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "chevron"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
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
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onToggle)
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(teamColor),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = teamLogoUrl(team.id),
                            contentDescription = "${team.name} logo",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = team.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${team.points} pts",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = teamColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Collapse" else "Expand",
                        modifier = Modifier.rotate(chevronRotation)
                    )
                }
                AnimatedVisibility(visible = expanded) {
                    Column {
                        HorizontalDivider()
                        team.drivers.forEach { driver ->
                            DriverRow(driver = driver, teamColor = teamColor)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DriverRow(driver: StandingsDriver, teamColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(teamColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = driver.code,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = teamColor
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = driver.code,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = driver.name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = "${driver.points} pts",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
