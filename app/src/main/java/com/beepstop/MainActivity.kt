package com.beepstop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.beepstop.ui.components.BottomNavBar
import com.beepstop.ui.screen.CustomizeScreen
import com.beepstop.ui.screen.RacesScreen
import com.beepstop.ui.screen.StandingsScreen
import com.beepstop.ui.theme.BeepStopTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BeepStopTheme {
                var selectedTab by rememberSaveable { mutableIntStateOf(0) }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavBar(
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                    }
                ) { innerPadding ->
                    when (selectedTab) {
                        0 -> RacesScreen(modifier = Modifier.padding(innerPadding))
                        1 -> StandingsScreen(modifier = Modifier.padding(innerPadding))
                        2 -> CustomizeScreen(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}
