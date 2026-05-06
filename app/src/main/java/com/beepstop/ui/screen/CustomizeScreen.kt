package com.beepstop.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.beepstop.ui.components.CircuitMapPreview
import com.beepstop.ui.components.ColorSwatchPicker
import com.beepstop.ui.theme.F1TeamColors
import com.beepstop.ui.theme.toComposeColor
import com.beepstop.ui.viewmodel.CustomizeViewModel

private val teams = listOf(
    "mercedes", "ferrari", "mclaren", "red_bull", "aston_martin",
    "alpine", "williams", "haas", "sauber", "rb", "cadillac"
)

private val teamLabels = mapOf(
    "mercedes" to "Mercedes", "ferrari" to "Ferrari", "mclaren" to "McLaren",
    "red_bull" to "Red Bull", "aston_martin" to "Aston Martin", "alpine" to "Alpine",
    "williams" to "Williams", "haas" to "Haas", "sauber" to "Audi/Sauber",
    "rb" to "RB", "cadillac" to "Cadillac"
)

private val drivers = listOf(
    "russell", "antonelli", "leclerc", "hamilton", "norris", "piastri",
    "verstappen", "lawson", "alonso", "stroll", "ocon", "doohan", "albon",
    "sainz", "hulkenberg", "bortoleto", "tsunoda", "hadjar", "bearman",
    "oliver", "colapinto", "crawford"
)

private val driverLabels = mapOf(
    "russell" to "Russell", "antonelli" to "Antonelli", "leclerc" to "Leclerc",
    "hamilton" to "Hamilton", "norris" to "Norris", "piastri" to "Piastri",
    "verstappen" to "Verstappen", "lawson" to "Lawson", "alonso" to "Alonso",
    "stroll" to "Stroll", "ocon" to "Ocon", "doohan" to "Doohan", "albon" to "Albon",
    "sainz" to "Sainz", "hulkenberg" to "Hülkenberg", "bortoleto" to "Bortoleto",
    "tsunoda" to "Tsunoda", "hadjar" to "Hadjar", "bearman" to "Bearman",
    "oliver" to "Oliver", "colapinto" to "Colapinto", "crawford" to "Crawford"
)

private val countdownTargets = listOf("all", "race", "qualifying", "sprint", "practice")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizeScreen(
    viewModel: CustomizeViewModel,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val bgColor by viewModel.pendingBgColor.collectAsState()
    val fontColor by viewModel.pendingFontColor.collectAsState()
    val circuitColor by viewModel.pendingCircuitColor.collectAsState()
    val nextCircuitId by viewModel.nextCircuitId.collectAsState()
    val smallMode by viewModel.pendingStandingsSmallMode.collectAsState()
    val smallId by viewModel.pendingStandingsSmallId.collectAsState()
    val largeMode by viewModel.pendingStandingsLargeMode.collectAsState()
    val countdownTarget by viewModel.pendingCountdownTarget.collectAsState()
    val countdownBg by viewModel.pendingCountdownBgColor.collectAsState()
    val countdownFont by viewModel.pendingCountdownFontColor.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.snackbarEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Customize Widgets") })
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            item {
                SectionCard(title = "Circuit Widget") {
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        CircuitMapPreview(
                            circuitId = nextCircuitId.ifBlank { "bahrain" },
                            tintColor = circuitColor.toComposeColor(),
                            bgColor = bgColor.toComposeColor(),
                            size = 100.dp
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    ColorPickerRow(label = "Background", selectedHex = bgColor, onSelect = viewModel::setBgColor)
                    Spacer(modifier = Modifier.height(12.dp))
                    ColorPickerRow(label = "Text", selectedHex = fontColor, onSelect = viewModel::setFontColor)
                    Spacer(modifier = Modifier.height(12.dp))
                    ColorPickerRow(label = "Circuit Line", selectedHex = circuitColor, onSelect = viewModel::setCircuitColor)
                }
            }

            item {
                SectionCard(title = "Standings Widget — Small") {
                    ModeToggle(mode = smallMode, onModeChange = viewModel::setStandingsSmallMode)
                    Spacer(modifier = Modifier.height(8.dp))
                    val items = if (smallMode == "team") teams to teamLabels else drivers to driverLabels
                    ItemDropdown(
                        label = if (smallMode == "team") "Team" else "Driver",
                        items = items.first,
                        labels = items.second,
                        selected = smallId,
                        onSelected = viewModel::setStandingsSmallId
                    )
                }
            }

            item {
                SectionCard(title = "Standings Widget — Medium") {
                    ModeToggle(mode = largeMode, onModeChange = viewModel::setStandingsLargeMode)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Select up to 3 items via the small widget pickers above.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                SectionCard(title = "Countdown Widget") {
                    Text(
                        text = "Session target",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    ItemDropdown(
                        label = "Target",
                        items = countdownTargets,
                        labels = countdownTargets.associateWith { it.replaceFirstChar { c -> c.uppercase() } },
                        selected = countdownTarget,
                        onSelected = viewModel::setCountdownTarget
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    ColorPickerRow(label = "Background", selectedHex = countdownBg, onSelect = viewModel::setCountdownBgColor)
                    Spacer(modifier = Modifier.height(12.dp))
                    ColorPickerRow(label = "Text", selectedHex = countdownFont, onSelect = viewModel::setCountdownFontColor)
                }
            }

            item {
                Button(
                    onClick = { viewModel.saveAll() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Apply")
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        androidx.compose.foundation.layout.Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun ColorPickerRow(label: String, selectedHex: String, onSelect: (String) -> Unit) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(modifier = Modifier.height(6.dp))
    ColorSwatchPicker(
        swatches = F1TeamColors.pickerSwatches,
        selectedHex = selectedHex,
        onColorSelected = onSelect
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModeToggle(mode: String, onModeChange: (String) -> Unit) {
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        listOf("team", "driver").forEachIndexed { index, value ->
            SegmentedButton(
                selected = mode == value,
                onClick = { onModeChange(value) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = 2),
                label = { Text(value.replaceFirstChar { it.uppercase() }) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemDropdown(
    label: String,
    items: List<String>,
    labels: Map<String, String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = labels[selected] ?: selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(androidx.compose.material3.ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(labels[item] ?: item) },
                    onClick = {
                        onSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}
