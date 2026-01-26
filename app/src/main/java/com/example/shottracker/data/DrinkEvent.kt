package com.example.shottracker.data

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class DrinkEvent(
    val id: String = UUID.randomUUID().toString(),
    val personId: String,
    val personName: String,
    val drink: Drink,
    val timestamp: LocalDateTime = LocalDateTime.now()
) {
    fun formatTime(): String {
        return timestamp.format(DateTimeFormatter.ofPattern("HH:mm"))
    }
}
