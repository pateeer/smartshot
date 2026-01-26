package com.example.shottracker.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Liquor
import androidx.compose.material.icons.filled.WineBar
import androidx.compose.material.icons.filled.SportsBar
import androidx.compose.ui.graphics.vector.ImageVector

data class Drink(
    val name: String,
    val alcoholPercentage: Double,
    val volumeMl: Int,
    val icon: ImageVector
) {
    val alcoholGrams: Double
        get() = volumeMl * (alcoholPercentage / 100.0) * 0.789  // 0.789 g/ml is alcohol density

    companion object {
        val PRESETS = listOf(
            Drink("Pivo", 5.0, 500, Icons.Default.SportsBar),
            Drink("Víno", 12.0, 150, Icons.Default.WineBar),
            Drink("Poldecák", 40.0, 40, Icons.Default.LocalBar),
            Drink("Koktail", 15.0, 200, Icons.Default.LocalDrink),
            Drink("Whiskey", 40.0, 50, Icons.Default.Liquor),
            Drink("Vodka", 40.0, 40, Icons.Default.LocalBar),
            Drink("Tequila", 38.0, 40, Icons.Default.Liquor),
            Drink("Šampanské", 12.0, 120, Icons.Default.WineBar)
        )
    }
}
