package com.example.shottracker.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.LocalBar
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.shottracker.MainViewModel
import com.example.shottracker.navigation.Screen
import com.example.shottracker.ui.theme.*

data class BottomNavItem(
    val screen: Screen,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun MainAppScreen(
    viewModel: MainViewModel
) {
    val navItems = listOf(
        BottomNavItem(Screen.Home, Icons.Filled.LocalBar, Icons.Outlined.LocalBar),
        BottomNavItem(Screen.Session, Icons.Filled.Timeline, Icons.Outlined.Timeline),
        BottomNavItem(Screen.Leaderboard, Icons.Filled.EmojiEvents, Icons.Outlined.EmojiEvents),
        BottomNavItem(Screen.Settings, Icons.Filled.Settings, Icons.Outlined.Settings)
    )
    
    var selectedRoute by remember { mutableStateOf(Screen.Home.route) }
    
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = SurfaceDark,
                contentColor = TextPrimary
            ) {
                navItems.forEach { item ->
                    val isSelected = selectedRoute == item.screen.route
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedRoute = item.screen.route },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.screen.title
                            )
                        },
                        label = { Text(item.screen.title) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Primary,
                            selectedTextColor = Primary,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted,
                            indicatorColor = Primary.copy(alpha = 0.2f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        when (selectedRoute) {
            Screen.Home.route -> {
                ShotTrackerScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            Screen.Session.route -> {
                val sessionStart by viewModel.sessionStartTime
                SessionOverviewScreen(
                    totalShots = viewModel.totalShots,
                    totalVolumeMl = viewModel.totalVolumeMl,
                    totalAlcoholGrams = viewModel.totalAlcoholGrams,
                    drinkHistory = viewModel.drinkHistory,
                    sessionStartTime = sessionStart,
                    onResetSession = { viewModel.resetSession() },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            Screen.Leaderboard.route -> {
                val selectedDrink by viewModel.selectedDrink
                LeaderboardScreen(
                    people = viewModel.people,
                    drink = selectedDrink,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            Screen.Settings.route -> {
                val settings by viewModel.settings
                SettingsScreen(
                    settings = settings,
                    onSettingsChange = { viewModel.updateSettings(it) },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
