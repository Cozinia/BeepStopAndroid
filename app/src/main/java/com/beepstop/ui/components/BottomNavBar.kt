package com.beepstop.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

private data class NavTab(val label: String, val icon: ImageVector)

private val tabs = listOf(
    NavTab("Races", Icons.Default.DateRange),
    NavTab("Standings", Icons.Default.List),
    NavTab("Customize", Icons.Default.Palette),
)

@Composable
fun BottomNavBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        tabs.forEachIndexed { index, tab ->
            val selected = selectedTab == index
            val scale by animateFloatAsState(
                targetValue = if (selected) 1f else 0.88f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                ),
                label = "tab_scale_$index"
            )
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(index) },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.label,
                        modifier = Modifier
                            .size(24.dp)
                            .scale(scale)
                    )
                },
                label = { Text(tab.label, style = MaterialTheme.typography.labelSmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            )
        }
    }
}
