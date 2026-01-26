package com.example.shottracker.data

import java.util.UUID

enum class Gender(val displayName: String, val bodyWaterConstant: Double) {
    MALE("Muž", 0.68),
    FEMALE("Žena", 0.55)
}

data class Person(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val heightCm: Int,
    val weightKg: Double,
    val gender: Gender,
    val shotCount: Int = 0
)
