package com.example.shottracker.util

import com.example.shottracker.data.Drink
import com.example.shottracker.data.Person
import kotlin.math.max

/**
 * Blood Alcohol Content calculator using the Widmark formula.
 * 
 * BAC = (alcohol_consumed_grams / (body_weight_grams × body_water_constant)) × 100
 * 
 * Metabolism rate: approximately 0.015% per hour
 */
object BacCalculator {
    
    private const val METABOLISM_RATE_PER_HOUR = 0.015
    
    data class BacResult(
        val bac: Double,
        val hoursUntilSober: Double,
        val intoxicationLevel: IntoxicationLevel
    )
    
    enum class IntoxicationLevel(val displayName: String, val description: String) {
        SOBER("Triezvy", "Žiadne účinky"),
        BUZZED("Veselý", "Mierny pocit eufórie, relaxácia"),
        TIPSY("Podgurážený", "Znížené zábrany, zhoršená koordinácia"),
        DRUNK("Opitý", "Výrazné zhoršenie schopností, nezrozumiteľná reč"),
        VERY_DRUNK("Veľmi opitý", "Ťažká opitosť, možné výpadky pamäte"),
        DANGEROUS("Nebezpečné", "Riziko otrávenia alkoholom!")
    }
    
    /**
     * Calculate BAC based on person's stats and drinks consumed.
     * 
     * @param person The person with weight and gender info
     * @param drink The type of drink being consumed
     * @param hoursSinceFirstDrink Hours elapsed since first drink (for metabolism)
     * @return BacResult with current BAC, time until sober, and intoxication level
     */
    fun calculate(
        person: Person,
        drink: Drink,
        hoursSinceFirstDrink: Double = 0.0
    ): BacResult {
        if (person.shotCount == 0) {
            return BacResult(0.0, 0.0, IntoxicationLevel.SOBER)
        }
        
        // Total alcohol in grams
        val totalAlcoholGrams = drink.alcoholGrams * person.shotCount
        
        // Body weight in grams
        val bodyWeightGrams = person.weightKg * 1000
        
        // Calculate BAC using Widmark formula
        val rawBac = (totalAlcoholGrams / (bodyWeightGrams * person.gender.bodyWaterConstant)) * 100
        
        // Account for metabolism over time
        val metabolizedBac = hoursSinceFirstDrink * METABOLISM_RATE_PER_HOUR
        val currentBac = max(0.0, rawBac - metabolizedBac)
        
        // Calculate hours until sober
        val hoursUntilSober = if (currentBac > 0) {
            currentBac / METABOLISM_RATE_PER_HOUR
        } else {
            0.0
        }
        
        // Determine intoxication level
        val level = when {
            currentBac < 0.02 -> IntoxicationLevel.SOBER
            currentBac < 0.05 -> IntoxicationLevel.BUZZED
            currentBac < 0.08 -> IntoxicationLevel.TIPSY
            currentBac < 0.15 -> IntoxicationLevel.DRUNK
            currentBac < 0.25 -> IntoxicationLevel.VERY_DRUNK
            else -> IntoxicationLevel.DANGEROUS
        }
        
        return BacResult(currentBac, hoursUntilSober, level)
    }
    
    /**
     * Format BAC as a percentage string.
     */
    fun formatBac(bac: Double): String {
        return String.format("%.3f%%", bac)
    }
    
    /**
     * Format hours until sober as readable string.
     */
    fun formatTimeUntilSober(hours: Double): String {
        return when {
            hours < 0.017 -> "Triezvy"
            hours < 1 -> "${(hours * 60).toInt()} min"
            else -> {
                val h = hours.toInt()
                val m = ((hours - h) * 60).toInt()
                if (m > 0) "${h}h ${m}m" else "${h}h"
            }
        }
    }
}
