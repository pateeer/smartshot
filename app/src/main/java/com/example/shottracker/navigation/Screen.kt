package com.example.shottracker.navigation

sealed class Screen(val route: String, val title: String) {
    data object Home : Screen("home", "Sledovanie")
    data object Session : Screen("session", "Aktuálna relácia")
    data object Leaderboard : Screen("leaderboard", "Rebríček")
    data object Settings : Screen("settings", "Nastavenia")
}
