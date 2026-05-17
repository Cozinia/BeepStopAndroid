package com.beepstop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.beepstop.ui.components.BottomNavBar
import com.beepstop.ui.screen.CustomizeScreen
import com.beepstop.ui.screen.DriversScreen
import com.beepstop.ui.screen.RacesScreen
import com.beepstop.ui.screen.StandingsScreen
import com.beepstop.ui.theme.BeepStopTheme
import com.beepstop.ui.viewmodel.CustomizeViewModel
import com.beepstop.ui.viewmodel.DriverViewModel
import com.beepstop.ui.viewmodel.RacesViewModel
import com.beepstop.ui.viewmodel.StandingsViewModel

class MainActivity : ComponentActivity() {

    private val racesViewModel: RacesViewModel by viewModels {
        RacesViewModel.factory((application as BeepStopApp).raceRepository)
    }

    private val standingsViewModel: StandingsViewModel by viewModels {
        StandingsViewModel.factory((application as BeepStopApp).standingsRepository)
    }

    private val driverViewModel: DriverViewModel by viewModels {
        DriverViewModel.factory((application as BeepStopApp).standingsRepository)
    }

    private val customizeViewModel: CustomizeViewModel by viewModels {
        CustomizeViewModel.factory((application as BeepStopApp).preferencesManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BeepStopTheme {
                var selectedTab by rememberSaveable { mutableIntStateOf(0) }
                val snackbarHostState = remember { SnackbarHostState() }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavBar(
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                    },
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState) { data ->
                            Snackbar(
                                snackbarData = data,
                                shape = RoundedCornerShape(12.dp),
                                containerColor = Color(0xFF34C759),
                                contentColor = Color.White,
                                actionContentColor = Color.White,
                                dismissActionContentColor = Color.White,
                            )
                        }
                    }
                ) { innerPadding ->
                    when (selectedTab) {
                        0 -> RacesScreen(viewModel = racesViewModel, modifier = Modifier.padding(innerPadding))
                        1 -> DriversScreen(viewModel = driverViewModel, modifier = Modifier.padding(innerPadding))
                        2 -> StandingsScreen(viewModel = standingsViewModel, modifier = Modifier.padding(innerPadding))
                        3 -> CustomizeScreen(
                            viewModel = customizeViewModel,
                            snackbarHostState = snackbarHostState,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
